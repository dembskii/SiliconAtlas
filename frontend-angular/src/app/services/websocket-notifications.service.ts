import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Subscription } from 'rxjs';
import { environment } from '../environment';
import { AuthService } from './auth.service';
import { NotificationDraft, NotificationStoreService } from './notification-store.service';

type NotificationSource = NotificationDraft['source'];

@Injectable({ providedIn: 'root' })
export class WebSocketNotificationsService {
  private static readonly RECONNECT_MAX_DELAY_MS = 30000;

  private client: Client | null = null;
  private reconnectTimer: ReturnType<typeof setTimeout> | null = null;
  private reconnectAttempt = 0;
  private shouldStayConnected = false;
  private isConnected = false;
  private started = false;

  private readonly authSubscription: Subscription;

  constructor(
    private authService: AuthService,
    private notificationStore: NotificationStoreService
  ) {
    this.authSubscription = this.authService.currentUser$.subscribe(() => {
      if (this.started) {
        this.syncConnectionState();
      }
    });
  }

  ensureStarted(): void {
    if (this.started) {
      return;
    }

    this.started = true;
    this.notificationStore.prune();
    this.addSystemNotification('WS_STARTED', 'Notifications service started');
    this.syncConnectionState();
  }

  forceDisconnect(): void {
    this.shouldStayConnected = false;
    this.clearReconnectTimer();

    if (!this.client) {
      return;
    }

    const currentClient = this.client;
    this.client = null;
    this.isConnected = false;

    currentClient.deactivate();
    this.addSystemNotification('WS_DISCONNECTED', 'Disconnected');
  }

  private syncConnectionState(): void {
    const hasSession = this.hasSession();
    if (!hasSession) {
      this.forceDisconnect();
      return;
    }

    this.shouldStayConnected = true;

    if (this.isConnected || this.client?.active) {
      return;
    }

    this.connect();
  }

  private connect(): void {
    if (!this.shouldStayConnected || this.client?.active) {
      return;
    }

    const token = this.authService.getToken();
    if (!token) {
      this.addSystemNotification('WS_SKIPPED', 'Missing access token, skipping connect');
      return;
    }

    const wsUrl = this.resolveWsUrl();

    this.client = new Client({
      webSocketFactory: () => new SockJS(wsUrl),
      connectHeaders: {
        Authorization: `Bearer ${token}`,
        authorization: `Bearer ${token}`
      },
      reconnectDelay: 0,
      onConnect: () => {
        this.isConnected = true;
        this.reconnectAttempt = 0;
        this.addSystemNotification('WS_CONNECTED', 'Connected');
        this.subscribeToTopics();
      },
      onStompError: (frame) => {
        this.isConnected = false;
        this.addSystemNotification('WS_STOMP_ERROR', `STOMP error: ${frame.headers['message'] ?? 'Unknown error'}`);
        this.scheduleReconnect('stomp-error');
      },
      onWebSocketClose: () => {
        this.isConnected = false;
        this.addSystemNotification('WS_CLOSED', 'Connection closed');
        this.scheduleReconnect('socket-close');
      },
      onWebSocketError: () => {
        this.isConnected = false;
        this.addSystemNotification('WS_TRANSPORT_ERROR', 'Transport error');
      }
    });

    this.addSystemNotification('WS_CONNECTING', `Connecting to ${wsUrl}`);
    this.client.activate();
  }

  private subscribeToTopics(): void {
    if (!this.client) {
      return;
    }

    this.client.subscribe('/topic/cpu-events', (message) => this.handleMessage('cpu', message));
    this.client.subscribe('/topic/technology-events', (message) => this.handleMessage('technology', message));
    this.client.subscribe('/topic/manufacturer-events', (message) => this.handleMessage('manufacturer', message));
    this.client.subscribe('/topic/all-events', (message) => this.handleMessage('all', message));
  }

  private handleMessage(source: NotificationSource, message: IMessage): void {
    const payload = this.parseMessageBody(message.body);
    const eventType = this.stringField(payload, 'eventType') ?? 'EVENT';
    const details = this.stringField(payload, 'details');
    const label = this.resolvePrimaryLabel(source, payload);

    let text = `${eventType}: ${label}`;
    if (details) {
      text = `${text} - ${details}`;
    }

    const timestamp = this.resolveTimestamp(payload);
    this.notificationStore.add({
      source,
      eventType,
      message: text,
      timestamp,
      raw: payload
    });
  }

  private scheduleReconnect(reason: string): void {
    if (!this.shouldStayConnected || !this.hasSession()) {
      return;
    }

    this.clearReconnectTimer();
    this.reconnectAttempt += 1;

    const delay = Math.min(
      1000 * Math.pow(2, this.reconnectAttempt - 1),
      WebSocketNotificationsService.RECONNECT_MAX_DELAY_MS
    );

    this.addSystemNotification(
      'WS_RECONNECT_SCHEDULED',
      `Reconnect in ${delay}ms (attempt ${this.reconnectAttempt}, reason: ${reason})`
    );

    this.reconnectTimer = setTimeout(() => {
      if (!this.shouldStayConnected || !this.hasSession()) {
        return;
      }
      this.connect();
    }, delay);
  }

  private clearReconnectTimer(): void {
    if (!this.reconnectTimer) {
      return;
    }

    clearTimeout(this.reconnectTimer);
    this.reconnectTimer = null;
  }

  private hasSession(): boolean {
    return !!this.authService.currentUser && !!this.authService.getToken() && !!this.authService.getRefreshToken();
  }

  private resolveWsUrl(): string {
    return `${environment.apiUrl.replace(/\/api\/v1\/?$/, '')}/ws/events`;
  }

  private parseMessageBody(body: string): unknown {
    try {
      return JSON.parse(body) as unknown;
    } catch {
      return { details: body };
    }
  }

  private resolveTimestamp(payload: unknown): string {
    const ts = this.stringField(payload, 'timestamp');
    if (ts && !Number.isNaN(Date.parse(ts))) {
      return new Date(ts).toISOString();
    }
    return new Date().toISOString();
  }

  private resolvePrimaryLabel(source: NotificationSource, payload: unknown): string {
    if (source === 'cpu') {
      return this.stringField(payload, 'cpuModel') ?? 'CPU event';
    }
    if (source === 'technology') {
      return this.stringField(payload, 'technologyName') ?? 'Technology event';
    }
    if (source === 'manufacturer') {
      return this.stringField(payload, 'manufacturerName') ?? 'Manufacturer event';
    }

    return this.stringField(payload, 'details') ?? this.stringField(payload, 'eventType') ?? 'Event';
  }

  private stringField(payload: unknown, key: string): string | null {
    if (!payload || typeof payload !== 'object') {
      return null;
    }

    const value = (payload as Record<string, unknown>)[key];
    return typeof value === 'string' ? value : null;
  }

  private addSystemNotification(eventType: string, message: string): void {
    this.notificationStore.add({
      source: 'system',
      eventType,
      message,
      timestamp: new Date().toISOString()
    });
  }
}

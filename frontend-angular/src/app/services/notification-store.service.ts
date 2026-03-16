import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface NotificationItem {
  id: string;
  source: 'cpu' | 'technology' | 'manufacturer' | 'all' | 'system';
  eventType: string;
  message: string;
  timestamp: string;
  raw?: unknown;
}

export interface NotificationDraft {
  source: NotificationItem['source'];
  eventType: string;
  message: string;
  timestamp?: string;
  raw?: unknown;
}

@Injectable({ providedIn: 'root' })
export class NotificationStoreService {
  private static readonly STORAGE_KEY = 'silicon-atlas.notifications.v1';
  private static readonly TTL_MS = 24 * 60 * 60 * 1000;
  private static readonly MAX_ITEMS = 50;

  private readonly notificationsSubject = new BehaviorSubject<NotificationItem[]>([]);
  readonly notifications$ = this.notificationsSubject.asObservable();

  constructor() {
    this.load();
  }

  load(): void {
    const raw = localStorage.getItem(NotificationStoreService.STORAGE_KEY);
    if (!raw) {
      this.notificationsSubject.next([]);
      return;
    }

    try {
      const parsed = JSON.parse(raw) as NotificationItem[];
      const normalized = Array.isArray(parsed)
        ? parsed.filter((item) => !!item?.id && !!item?.message && !!item?.timestamp)
        : [];
      const pruned = this.pruneList(normalized);
      this.notificationsSubject.next(pruned);
      this.persist(pruned);
    } catch {
      this.notificationsSubject.next([]);
      localStorage.removeItem(NotificationStoreService.STORAGE_KEY);
    }
  }

  add(draft: NotificationDraft): void {
    const item: NotificationItem = {
      id: this.generateId(),
      source: draft.source,
      eventType: draft.eventType,
      message: draft.message,
      timestamp: draft.timestamp ?? new Date().toISOString(),
      raw: draft.raw
    };

    const next = this.pruneList([item, ...this.notificationsSubject.value]);
    this.notificationsSubject.next(next);
    this.persist(next);
  }

  prune(): void {
    const pruned = this.pruneList(this.notificationsSubject.value);
    this.notificationsSubject.next(pruned);
    this.persist(pruned);
  }

  clear(): void {
    this.notificationsSubject.next([]);
    localStorage.removeItem(NotificationStoreService.STORAGE_KEY);
  }

  private pruneList(items: NotificationItem[]): NotificationItem[] {
    const now = Date.now();
    const ttlBoundary = now - NotificationStoreService.TTL_MS;

    return [...items]
      .filter((item) => {
        const ts = Date.parse(item.timestamp);
        return !Number.isNaN(ts) && ts >= ttlBoundary;
      })
      .sort((a, b) => Date.parse(b.timestamp) - Date.parse(a.timestamp))
      .slice(0, NotificationStoreService.MAX_ITEMS);
  }

  private persist(items: NotificationItem[]): void {
    localStorage.setItem(NotificationStoreService.STORAGE_KEY, JSON.stringify(items));
  }

  private generateId(): string {
    if (typeof crypto !== 'undefined' && typeof crypto.randomUUID === 'function') {
      return crypto.randomUUID();
    }
    return `${Date.now()}-${Math.random().toString(36).slice(2)}`;
  }
}

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
    if (draft.source === 'system') {
      return;
    }

    const item: NotificationItem = {
      id: this.generateId(),
      source: draft.source,
      eventType: draft.eventType,
      message: draft.message,
      timestamp: draft.timestamp ?? new Date().toISOString(),
      raw: draft.raw
    };

    const duplicate = this.notificationsSubject.value.some((existing) => {
      const sameEventId = this.extractEventId(existing.raw) !== null && this.extractEventId(existing.raw) === this.extractEventId(item.raw);
      const samePayload = existing.eventType === item.eventType && existing.message === item.message && existing.timestamp === item.timestamp;
      return sameEventId || samePayload;
    });

    if (duplicate) {
      return;
    }

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
      .filter((item) => item.source !== 'system')
      .filter((item) => {
        const ts = this.toEpochMs(item.timestamp);
        if (ts > 0) {
          return ts >= ttlBoundary;
        }

        return false;
      })
      .sort((a, b) => this.toEpochMs(b.timestamp) - this.toEpochMs(a.timestamp))
      .slice(0, NotificationStoreService.MAX_ITEMS);
  }

  private extractEventId(raw: unknown): string | null {
    if (!raw || typeof raw !== 'object') {
      return null;
    }

    const eventId = (raw as Record<string, unknown>)['eventId'];
    return typeof eventId === 'string' && eventId.length > 0 ? eventId : null;
  }

  private toEpochMs(value: string): number {
    if (this.hasExplicitTimezone(value)) {
      const parsed = Date.parse(value);
      if (!Number.isNaN(parsed)) {
        return parsed;
      }
    }

    const utc = this.parseUtcDateTime(value);
    return utc ?? 0;
  }

  private hasExplicitTimezone(value: string): boolean {
    return /([zZ]|[+-]\d{2}:\d{2})$/.test(value);
  }

  private parseUtcDateTime(value: string): number | null {
    const match = value.match(/^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2})(?::(\d{2})(?:\.(\d{1,3}))?)?$/);
    if (!match) {
      return null;
    }

    const [, year, month, day, hour, minute, second, millisecond] = match;
    const epoch = Date.UTC(
      Number(year),
      Number(month) - 1,
      Number(day),
      Number(hour),
      Number(minute),
      Number(second ?? '0'),
      Number((millisecond ?? '0').padEnd(3, '0'))
    );

    return Number.isNaN(epoch) ? null : epoch;
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

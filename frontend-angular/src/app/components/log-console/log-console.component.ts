import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import {
  NotificationItem,
  NotificationStoreService
} from '../../services/notification-store.service';
import { WebSocketNotificationsService } from '../../services/websocket-notifications.service';

@Component({
  selector: 'app-log-console',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './log-console.component.html',
  styleUrl: './log-console.component.scss'
})
export class LogConsoleComponent implements OnInit, OnDestroy {
  private static readonly TIMESTAMP_FORMATTER = new Intl.DateTimeFormat(undefined, {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  });

  expanded = true;
  search = '';

  notifications: NotificationItem[] = [];

  private readonly subscriptions = new Subscription();

  constructor(
    private notificationStore: NotificationStoreService,
    private websocketNotificationsService: WebSocketNotificationsService
  ) {}

  ngOnInit(): void {
    this.websocketNotificationsService.ensureStarted();

    this.subscriptions.add(
      this.notificationStore.notifications$.subscribe((items) => {
        this.notifications = items;
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  toggleExpanded(): void {
    this.expanded = !this.expanded;
  }

  clearNotifications(): void {
    this.notificationStore.clear();
  }

  formatTimestamp(isoString: string): string {
    const epochMs = this.toEpochMs(isoString);
    if (epochMs <= 0) {
      return isoString;
    }

    return LogConsoleComponent.TIMESTAMP_FORMATTER.format(new Date(epochMs));
  }

  get filteredNotifications(): NotificationItem[] {
    const query = this.search.trim().toLowerCase();
    const filtered = !query ? this.notifications : this.notifications.filter((notification) => {
      return (
        notification.message.toLowerCase().includes(query) ||
        notification.eventType.toLowerCase().includes(query) ||
        notification.source.toLowerCase().includes(query)
      );
    });

    // Show newest notifications at the bottom.
    return [...filtered].sort((a, b) => this.toEpochMs(a.timestamp) - this.toEpochMs(b.timestamp));
  }

  private toEpochMs(value: string): number {
    if (this.hasExplicitTimezone(value)) {
      const parsed = Date.parse(value);
      if (!Number.isNaN(parsed)) {
        return parsed;
      }
    }

    const localDateTime = value.match(/^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2})(?::(\d{2})(?:\.(\d{1,3}))?)?$/);
    if (!localDateTime) {
      return 0;
    }

    const [, year, month, day, hour, minute, second, millisecond] = localDateTime;
    return Date.UTC(
      Number(year),
      Number(month) - 1,
      Number(day),
      Number(hour),
      Number(minute),
      Number(second ?? '0'),
      Number((millisecond ?? '0').padEnd(3, '0'))
    );
  }

  private hasExplicitTimezone(value: string): boolean {
    return /([zZ]|[+-]\d{2}:\d{2})$/.test(value);
  }
}

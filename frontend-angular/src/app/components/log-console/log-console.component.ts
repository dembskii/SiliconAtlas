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
    return new Date(isoString).toLocaleString();
  }

  get filteredNotifications(): NotificationItem[] {
    const query = this.search.trim().toLowerCase();
    if (!query) {
      return this.notifications;
    }
    return this.notifications.filter((notification) => {
      return (
        notification.message.toLowerCase().includes(query) ||
        notification.eventType.toLowerCase().includes(query) ||
        notification.source.toLowerCase().includes(query)
      );
    });
  }
}

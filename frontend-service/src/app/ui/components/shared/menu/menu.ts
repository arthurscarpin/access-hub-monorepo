import { Component, Input, signal } from '@angular/core';
import { LucideBell } from '@lucide/angular';

type NotificationItem = {
  id: number;
  title: string;
  description: string;
  status: 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED';
  read: boolean;
};

@Component({
  standalone: true,
  selector: 'app-menu',
  imports: [LucideBell],
  templateUrl: './menu.html',
})
export class Menu {
  @Input() screenOperation!: string;
  @Input() screenName!: string;

  isOpen = signal(false);

  notifications = signal<NotificationItem[]>([
    {
      id: 1,
      title: 'Capture #1201',
      description: 'Status changed to PENDING',
      status: 'PENDING',
      read: false,
    },
    {
      id: 2,
      title: 'Capture #1202',
      description: 'Status changed to PROCESSING',
      status: 'PROCESSING',
      read: false,
    },
    {
      id: 3,
      title: 'Capture #1203',
      description: 'Status changed to COMPLETED',
      status: 'COMPLETED',
      read: true,
    },
  ]);

  toggle() {
    this.isOpen.update(v => !v);
  }

  unreadCount() {
    return this.notifications().filter(n => !n.read).length;
  }

  markAsRead(id: number) {
    this.notifications.update(list =>
      list.map(n => n.id === id ? { ...n, read: true } : n)
    );
  }

  markAllAsRead() {
    this.notifications.update(list =>
      list.map(n => ({ ...n, read: true }))
    );
  }
}
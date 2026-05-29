import { Component, output, effect, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WebsocketService } from '../../../core/services/websocket.service';

@Component({
  selector: 'app-shared-notification-dropdown',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './shared-notification-dropdown.html'
})
export class SharedNotificationDropdown {
  private readonly wsService = inject(WebsocketService);

  notifications = this.wsService.notifications;
  unreadCount = this.wsService.unreadCount;
  countChanged = output<number>();

  private readonly unreadCountEffect = effect(() => {
    this.countChanged.emit(this.unreadCount());
  });

  toggleReasoning(id: string, event: Event): void {
    event.stopPropagation();
    this.wsService.notifications.update((oldList) =>
      oldList.map((notif) => 
        notif.id === id ? { ...notif, isExpanded: !notif.isExpanded } : notif
      )
    );
  }

  markAllAsRead(): void { this.wsService.markAllAsRead(); }
  markAsRead(id: string): void { this.wsService.markAsRead(id); }
  clearAll(): void { this.wsService.clearAll(); }
}
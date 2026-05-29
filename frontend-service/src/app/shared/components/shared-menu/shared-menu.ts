import { Component, Input, signal } from '@angular/core';
import { LucideBell } from '@lucide/angular';
import { SharedMenuConfig } from './shared-menu.config';
import { SharedNotificationDropdown } from '../shared-notification-dropdown/shared-notification-dropdown';

@Component({
  standalone: true,
  selector: 'app-shared-menu',
  imports: [LucideBell, SharedNotificationDropdown],
  templateUrl: './shared-menu.html'
})
export class SharedMenu {
  @Input() config!: SharedMenuConfig;

  public readonly dropdownStage = signal(false);
  
  public readonly notificationCount = signal<number>(0);

  public toggleDropdown(): void {
    this.dropdownStage.update((value) => !value);
  }

  public updateCount(count: number): void {
    this.notificationCount.set(count);
  }
}
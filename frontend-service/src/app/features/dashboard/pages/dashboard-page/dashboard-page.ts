import { Component, inject, signal, computed } from '@angular/core';
import { SharedSidebar } from '../../../../shared/components/shared-sidebar/shared-sidebar';
import { SharedMenu } from '../../../../shared/components/shared-menu/shared-menu';
import { SharedHeader } from '../../../../shared/components/shared-header/shared-header';
import { SharedMenuConfig } from '../../../../shared/components/shared-menu/shared-menu.config';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  standalone: true,
  selector: 'app-dashboard-page',
  imports: [SharedSidebar, SharedMenu, SharedHeader],
  templateUrl: './dashboard-page.html',
})
export class DashboardPage {
  private readonly authService = inject(AuthService);

  public readonly modalStage = signal(false);

  public readonly username = signal(this.authService.getLoginInfo().username);

  private getGreeting(): string {
    const hour = new Date().getHours();

    if (hour >= 5 && hour < 12) return 'Good morning';
    if (hour >= 12 && hour < 18) return 'Good afternoon';
    return 'Good evening';
  }

  private capitalize(name: string): string {
    if (!name) return '';
    return name.charAt(0).toUpperCase() + name.slice(1).toLowerCase();
  }

  public readonly headerConfig = computed(() => ({
    category: 'Operation',
    title: `${this.getGreeting()}, welcome ${this.capitalize(this.username())}! 👋`,
    description: 'Your monitor operations',
  }));

  public readonly menuConfig: SharedMenuConfig = {
    category: 'Operation',
    title: 'Dashboard',
  };

  public openModal(): void {
    this.modalStage.set(true);
  }

  public closeModal(): void {
    this.modalStage.set(false);
  }
}

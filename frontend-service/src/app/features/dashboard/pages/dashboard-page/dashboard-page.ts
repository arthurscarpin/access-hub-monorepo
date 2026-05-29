import { Component, inject, signal, computed } from '@angular/core';
import { SharedSidebar } from '../../../../shared/components/shared-sidebar/shared-sidebar';
import { SharedMenu } from '../../../../shared/components/shared-menu/shared-menu';
import { SharedHeader } from '../../../../shared/components/shared-header/shared-header';
import { SharedMenuConfig } from '../../../../shared/components/shared-menu/shared-menu.config';
import { AuthService } from '../../../../core/services/auth.service';
import { DashboardCard } from '../../components/dashboard-card/dashboard-card';
import { DashboardCardConfig } from '../../components/dashboard-card/dashboard-card.config';
import { LucideActivity, LucideCircleUser, LucideCar } from '@lucide/angular';
import { VehicleService } from '../../../../core/services/vehicle.service';
import { OwnerService } from '../../../../core/services/owner.service';
import { AccessEventService } from '../../../../core/services/access-event.service';
import { DashboardEventsView } from '../../components/dashboard-events-view/dashboard-events-view';

@Component({
  standalone: true,
  selector: 'app-dashboard-page',
  imports: [SharedSidebar, SharedMenu, SharedHeader, DashboardCard, DashboardEventsView],
  templateUrl: './dashboard-page.html',
})
export class DashboardPage {
  private readonly authService = inject(AuthService);
  private readonly vehicleService = inject(VehicleService);
  private readonly ownerService = inject(OwnerService);
  private readonly accessEventService = inject(AccessEventService);

  public readonly modalStage = signal(false);

  public readonly username = signal(this.authService.getLoginInfo().username);

  public readonly totalAccessEvents = computed<DashboardCardConfig>(() => ({
    title: 'Registered acess events',
    value: this.accessEventService.totalElements(),
    description: 'Registered acess events in the system',
    icon: LucideActivity,
    color: 'blue'
  }));

  public readonly totalVehicles = computed<DashboardCardConfig>(() => ({
    title: 'Registered vehicles',
    value: this.vehicleService.totalElements(),
    description: 'Registered vehicles in the system',
    icon: LucideCar,
    color: 'red'
  }));

  public readonly totalOwners = computed<DashboardCardConfig>(() => ({
    title: 'Registered owners',
    value: this.ownerService.totalElements(),
    description: 'Registered owners in the system',
    icon: LucideCircleUser,
    color: 'amber'
  }));
  
  public readonly accessEvents = this.accessEventService.accessEvents;

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

  public ngOnInit() {
    this.vehicleService.findAll();
    this.ownerService.findAll();
    this.accessEventService.findAll(0, 4);
  }
}

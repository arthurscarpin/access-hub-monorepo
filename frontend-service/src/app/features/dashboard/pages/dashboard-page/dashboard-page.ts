import { Component, inject, signal } from '@angular/core';
import { SharedSidebar } from '../../../../shared/components/shared-sidebar/shared-sidebar';
import { SharedMenu } from '../../../../shared/components/shared-menu/shared-menu';
import { SharedHeader } from '../../../../shared/components/shared-header/shared-header';
import { SharedHeaderConfig } from '../../../../shared/components/shared-header/shared-header-config';
import { SharedMenuConfig } from '../../../../shared/components/shared-menu/shared-menu.config';

@Component({
  standalone: true,
  selector: 'app-dashboard-page',
  imports: [SharedSidebar, SharedMenu, SharedHeader,],
  templateUrl: './dashboard-page.html'
})
export class DashboardPage {
  public readonly modalStage = signal(false);

   public readonly headerConfig: SharedHeaderConfig = {
    category: 'Management',
    title: 'Vehicles',
    description: 'Control panel for vehicle management',
  };

  public readonly menuConfig: SharedMenuConfig = {
    category: 'Management',
    title: 'Vehicles',
  };

  public openModal(): void {
    this.modalStage.set(true);
  }

  public closeModal(): void {
    this.modalStage.set(false);
  }
}

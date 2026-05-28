import { Component, inject, signal } from '@angular/core';
import { SharedSidebar } from '../../../../shared/components/shared-sidebar/shared-sidebar';
import { SharedMenu } from '../../../../shared/components/shared-menu/shared-menu';
import { SharedHeader } from '../../../../shared/components/shared-header/shared-header';
import { SharedHeaderConfig } from '../../../../shared/components/shared-header/shared-header-config';
import { SharedMenuConfig } from '../../../../shared/components/shared-menu/shared-menu.config';
import { Vehicle } from '../../../../core/models/vehicle.model';
import { VehicleService } from '../../../../core/services/vehicle.service';
import { VehiclesListView } from '../../components/vehicles-list-view/vehicles-list-view';
import { VehiclesRegisterModal } from '../../components/vehicles-register-modal/vehicles-register-modal';

@Component({
  standalone: true,
  selector: 'app-vehicles-page',
  imports: [SharedSidebar, SharedMenu, SharedHeader, VehiclesListView, VehiclesRegisterModal],
  templateUrl: './vehicles-page.html',
})
export class VehiclesPage {
  vehicles = signal<Vehicle[]>([]);
  private readonly service = inject(VehicleService);
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

  public reload(): void {
    this.service.findAll();
  }
}

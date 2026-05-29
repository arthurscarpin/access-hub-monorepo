import { Component, inject } from '@angular/core';
import { NgClass } from '@angular/common';
import { VehicleService } from '../../../../core/services/vehicle.service';

@Component({
  standalone: true,
  selector: 'app-vehicles-list-view',
  imports: [NgClass],
  templateUrl: './vehicles-list-view.html',
})
export class VehiclesListView {
  private readonly vehicleService = inject(VehicleService);

  public readonly vehicles = this.vehicleService.vehicles;
  public readonly loading = this.vehicleService.loading;
  public readonly totalElements = this.vehicleService.totalElements;
  public readonly pagination = this.vehicleService.pagination;

  public ngOnInit(): void {
    this.vehicleService.findAll();
  }

  public changePage(action: 'next' | 'previous'): void {
    const pagination = this.pagination();

    if (!pagination) return;

    if (action === 'next' && pagination.last) return;
    if (action === 'previous' && pagination.first) return;

    const nextPage = action === 'next' ? pagination.number + 1 : pagination.number - 1;

    this.vehicleService.findAll(nextPage);
  }

  public toggleStatus(vehicleId: string) {
    this.vehicleService.updateById(vehicleId).subscribe({
      next: (updatedVehicle) => {
        this.vehicleService.updateVehicleInList(updatedVehicle);
      },
      error: (err) => {
        console.error('Error toggling status', err);
      },
    });
  }
}

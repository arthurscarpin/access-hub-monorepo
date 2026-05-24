import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { VehiclesTable } from '../../components/vehicles-table/vehicles-table';

@Component({
  selector: 'app-vehicles',
  standalone: true,
  imports: [Sidebar, VehiclesTable],
  templateUrl: './vehicles.html',
})
export class Vehicles{}
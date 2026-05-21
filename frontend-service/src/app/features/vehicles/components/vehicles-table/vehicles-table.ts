import { Component } from '@angular/core';
import { VehiclesTableItem } from '../vehicles-table-item/vehicles-table-item';

@Component({
  selector: 'app-vehicles-table',
  imports: [VehiclesTableItem],
  templateUrl: './vehicles-table.html'
})
export class VehiclesTable {}

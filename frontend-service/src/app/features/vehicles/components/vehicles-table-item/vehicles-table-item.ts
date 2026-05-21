import { Component } from '@angular/core';
import { LucideAngularModule, Ellipsis } from 'lucide-angular';

@Component({
  selector: '[app-vehicles-table-item]',
  imports: [LucideAngularModule],
  templateUrl: './vehicles-table-item.html'
})
export class VehiclesTableItem {
  readonly Ellipsis: any = Ellipsis;
}

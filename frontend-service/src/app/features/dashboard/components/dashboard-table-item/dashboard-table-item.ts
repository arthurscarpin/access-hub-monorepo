import { Component } from '@angular/core';
import { LucideAngularModule, ArrowUpRight } from 'lucide-angular';

@Component({
  selector: 'app-dashboard-table-item',
  imports: [LucideAngularModule],
  templateUrl: './dashboard-table-item.html',
})
export class DashboardTableItem {
  readonly ArrowUpRight = ArrowUpRight;
}

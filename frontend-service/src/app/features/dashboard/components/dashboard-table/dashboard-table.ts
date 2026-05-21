import { Component } from '@angular/core';
import { DashboardTableItem } from '../dashboard-table-item/dashboard-table-item';

import { LucideAngularModule, ArrowDownLeft } from 'lucide-angular';

@Component({
  selector: 'app-dashboard-table',
  imports: [LucideAngularModule, DashboardTableItem],
  templateUrl: './dashboard-table.html'
})
export class DashboardTable {
  readonly ArrowDownLeft = ArrowDownLeft;
}

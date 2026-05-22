import { Component } from '@angular/core';
import { DashboardTableItem } from '../dashboard-table-item/dashboard-table-item';

@Component({
  selector: 'app-dashboard-table',
  standalone: true,
  imports: [DashboardTableItem],
  templateUrl: './dashboard-table.html'
})
export class DashboardTable {}

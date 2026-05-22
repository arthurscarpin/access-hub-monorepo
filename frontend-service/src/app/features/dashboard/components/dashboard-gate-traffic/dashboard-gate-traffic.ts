import { Component } from '@angular/core';
import { DashboardGateTrafficItem } from '../dashboard-gate-traffic-item/dashboard-gate-traffic-item';

@Component({
  selector: 'app-dashboard-gate-traffic',
  imports: [DashboardGateTrafficItem],
  standalone: true,
  templateUrl: './dashboard-gate-traffic.html',
})
export class DashboardGateTraffic {}

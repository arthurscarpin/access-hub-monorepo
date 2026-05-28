import { Component, Input } from '@angular/core';
import { NgComponentOutlet } from '@angular/common';
import { DashboardCardConfig } from './dashboard-card.config';

@Component({
  standalone: true,
  selector: 'app-dashboard-card',
  imports: [NgComponentOutlet],
  templateUrl: './dashboard-card.html'
})
export class DashboardCard {
  @Input() config!: DashboardCardConfig;
}

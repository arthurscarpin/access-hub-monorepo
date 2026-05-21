import { Component } from '@angular/core';
import { LucideAngularModule, Activity } from 'lucide-angular';

@Component({
  selector: 'app-dashboard-card',
  imports: [LucideAngularModule],
  templateUrl: './dashboard-card.html'
})
export class DashboardCard {
  readonly Activity = Activity;
}

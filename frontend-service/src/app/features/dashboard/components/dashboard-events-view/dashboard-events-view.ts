import { Component, Input } from '@angular/core';
import { AccessEvent } from '../../../../core/models/access-event.model';

@Component({
  standalone: true,
  selector: 'app-dashboard-events-view',
  imports: [],
  templateUrl: './dashboard-events-view.html',
})
export class DashboardEventsView {
  @Input() events!: AccessEvent[];
}
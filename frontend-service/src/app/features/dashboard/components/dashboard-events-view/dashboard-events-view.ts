import { Component, Input } from '@angular/core';
import { AccessEvent } from '../../../../core/models/access-event.model';

type AccessEventView = AccessEvent & {
  resultNormalized: string;
};

@Component({
  standalone: true,
  selector: 'app-dashboard-events-view',
  imports: [],
  templateUrl: './dashboard-events-view.html',
})
export class DashboardEventsView {
  private _events: AccessEventView[] = [];

  @Input()
  set events(value: AccessEvent[]) {
    this._events = (value ?? []).map(event => ({
      ...event,
      resultNormalized: event.result?.toLowerCase(),
    }));
  }

  get events(): AccessEventView[] {
    return this._events;
  }
}
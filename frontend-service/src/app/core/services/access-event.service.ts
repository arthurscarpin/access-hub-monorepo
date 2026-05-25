import { Injectable, computed, signal, Type } from '@angular/core';
import { LucideArrowDownLeft, LucideArrowUpRight } from '@lucide/angular';
import { AccessEventResponse } from '@core/models/access-event.models';
import { ACCESS_EVENTS_MOCKY } from '@components/access-events/access-events-container/access-events-container.mock';
import { AccessEventConfig } from '@components/shared/interfaces/ui.interfaces';

@Injectable({ providedIn: 'root' })
export class AccessEventsService {
  
  private _events = signal<AccessEventResponse[]>([]);

  public events = computed<AccessEventConfig[]>(() => {
    return this._events().map((event) => ({
      id: event.id,
      plate: event.plate,
      direction: event.direction,
      status: event.status,
      date: event.date,
      icon: (event.direction === 'Entry' ? LucideArrowDownLeft : LucideArrowUpRight) as Type<any>,
    }));
  });

  constructor() {
    this.loadEvents();
  }

  private loadEvents() {
    this._events.set(ACCESS_EVENTS_MOCKY);
  }
}

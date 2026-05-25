import { Component, inject, signal, computed } from '@angular/core';

import { Button } from '@components/shared/button/button';
import { AccessEventsService } from '@core/services/access-event.service';
import { AccessEventsTable } from '@components/access-events/access-events-table/access-events-table';

import { 
  FILTER_OPTIONS, 
  PAGINATION_BUTTONS 
} from './access-events.constants';

@Component({
  standalone: true,
  selector: 'app-access-events-container',
  imports: [Button, AccessEventsTable],
  templateUrl: './access-events-container.html',
})
export class AccessEventsContainer{
  private eventsService = inject(AccessEventsService);
  
  public readonly filterOptions = FILTER_OPTIONS;
  public readonly paginationButtons = PAGINATION_BUTTONS;

  public selectedFilter = signal<string>('All');

  public filteredEvents = computed(() => {
    const rawEvents = this.eventsService.events();
    const currentFilter = this.selectedFilter();
    if (currentFilter === 'All') {
      return rawEvents;
    }
    return rawEvents.filter(event => 
      event.status === currentFilter || event.direction === currentFilter
    );
  });

  public selectFilter(filter: string): void {
    this.selectedFilter.set(filter);
  }

  public handlePagination(action: string): void {
    console.log(`Navigate to: ${action}`);
  }
}
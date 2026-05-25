import { Component, inject, signal, computed, OnInit } from '@angular/core';
import { Button } from '@ui/components/shared/button/button';
import { AccessEventsService } from '@core/services/access-event.service';
import { AccessEventsTable } from '@ui/components/access-events/access-events-table/access-events-table';
import { FILTER_OPTIONS, PAGINATION_BUTTONS } from './access-events.constants';


@Component({
  standalone: true,
  selector: 'app-access-events-container',
  imports: [Button, AccessEventsTable],
  templateUrl: './access-events-container.html',
})
export class AccessEventsContainer implements OnInit {
  private accessEventService = inject(AccessEventsService);
  private readonly pageSize = 8;
  private fetchEvents(): void {
    this.accessEventService.load(this.currentPage(), this.pageSize);
  }

  public readonly filterOptions = FILTER_OPTIONS;
  public readonly paginationButtons = PAGINATION_BUTTONS;
  public selectedFilter = signal<string>('All');
  public currentPage = signal<number>(0); 
  
  ngOnInit() {
    this.fetchEvents();
  }

  public filteredEvents = computed(() => {
    const rawEvents = this.accessEventService.events();
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
    const page = this.currentPage();
    if (action === 'next') {
      this.currentPage.set(page + 1);
    } else if (action === 'prev' && page > 0) {
      this.currentPage.set(page - 1);
    }
    this.fetchEvents();
  }
}
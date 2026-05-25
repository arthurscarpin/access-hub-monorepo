import { Component, inject, signal, computed, OnInit } from '@angular/core';
import { Button } from '@ui/components/shared/button/button';
import { AccessEventsService } from '@core/services/access-event.service';
import { AccessEventsTable } from '@ui/components/access-events/access-events-table/access-events-table';


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

  public readonly filterOptions = ['All', 'Authorized', 'Denied', 'In', 'Out'];
  public readonly paginationButtons = [ { label: 'Previous', action: 'prev' }, { label: 'Next', action: 'next' }];
  
  public selectedFilter = signal<string>('All');
  public currentPage = signal<number>(0);

  ngOnInit() {
    this.fetchEvents();
  }

  public filteredEvents = computed(() => {
    const rawEvents = this.accessEventService.accessEvents();
    const currentFilter = this.selectedFilter();
    if (currentFilter === 'All') {
      return rawEvents;
    }
    return rawEvents.filter(
      (event) => event.status === currentFilter || event.direction === currentFilter,
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

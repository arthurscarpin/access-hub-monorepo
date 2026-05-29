import { Component, computed, EventEmitter, Input, Output, signal } from '@angular/core';
import { DatePipe } from '@angular/common';

import { AccessEvent } from '../../../../core/models/access-event.model';
import { Paginated } from '../../../../core/models/paginated.model';

type FilterOption = 'All' | 'Authorized' | 'Denied';

@Component({
  standalone: true,
  selector: 'app-access-events-view',
  imports: [DatePipe],
  templateUrl: './access-events-view.html',
})
export class AccessEventsView {
  @Input({ required: true })
  set events(value: AccessEvent[]) {
    this._events.set(
      value.map((e) => ({
        ...e,
        resultNormalized: e.result?.toLowerCase(),
      })),
    );
  }

  private _events = signal<(AccessEvent & { resultNormalized: string })[]>([]);

  @Input({ required: true })
  pagination!: Paginated<AccessEvent> | null;

  @Output()
  pageChange = new EventEmitter<'previous' | 'next'>();

  public readonly filterOptions: FilterOption[] = ['All', 'Authorized', 'Denied'];

  public readonly selectedFilter = signal<FilterOption>('All');

  public readonly filteredEvents = computed(() => {
    const filter = this.selectedFilter();
    const events = this._events();

    if (filter === 'All') return events;

    const map = {
      Authorized: 'authorized',
      Denied: 'denied',
    } as const;

    return events.filter((e) => e.resultNormalized === map[filter]);
  });

  public selectFilter(filter: FilterOption): void {
    this.selectedFilter.set(filter);
  }

  public changePage(direction: 'previous' | 'next'): void {
    this.pageChange.emit(direction);
  }
}

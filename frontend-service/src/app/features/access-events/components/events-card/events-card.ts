import { Component } from '@angular/core';
import { EventFilters } from '../event-filters/event-filters';
import { EventTable } from '../event-table/event-table';
import { EventPagination } from '../event-pagination/event-pagination';


@Component({
  selector: 'app-events-card',
  imports: [
    EventFilters,
    EventTable,
    EventPagination
  ],
  templateUrl: './events-card.html',
  styleUrl: './events-card.css',
})
export class EventsCard {}
import { Component } from '@angular/core';
import { EventFilters } from '../event-filters/event-filters';
import { EventTable } from '../event-table/event-table';
import { EventPagination } from '../event-pagination/event-pagination';


@Component({
  selector: 'app-event-card',
  imports: [
    EventFilters,
    EventTable,
    EventPagination
  ],
  templateUrl: './event-card.html',
  styleUrl: './event-card.css',
})
export class EventCard {}
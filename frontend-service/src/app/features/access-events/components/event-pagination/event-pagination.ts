import { Component } from '@angular/core';
import { EventPaginationButton } from '../event-pagination-button/event-pagination-button';

@Component({
  selector: 'app-event-pagination',
  imports: [EventPaginationButton],
  templateUrl: './event-pagination.html',
  styleUrl: './event-pagination.css',
})
export class EventPagination {}

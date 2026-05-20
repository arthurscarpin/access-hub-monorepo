import { Component } from '@angular/core';
import { EventTableRow } from '../event-table-row/event-table-row';

@Component({
  selector: 'app-event-table',
  imports: [EventTableRow],
  templateUrl: './event-table.html',
  styleUrl: './event-table.css',
})
export class EventTable {}

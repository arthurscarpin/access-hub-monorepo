import { Component } from '@angular/core';
import { EventStatusBadge } from '../event-status-badge/event-status-badge';
import { EventDirectionBadge } from '../event-direction-badge/event-direction-badge';

@Component({
  standalone: true,
  selector: 'tr[app-event-table-row]',
  imports: [EventStatusBadge, EventDirectionBadge],
  templateUrl: './event-table-row.html',
})
export class EventTableRow {}
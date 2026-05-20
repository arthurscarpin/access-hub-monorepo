import { Component } from '@angular/core';
import { StatusBadge } from '../status-badge/status-badge';
import { DirectionBadge } from '../direction-badge/direction-badge';

@Component({
  standalone: true,
  selector: 'tr[app-event-table-row]',
  imports: [StatusBadge, DirectionBadge],
  templateUrl: './event-table-row.html',
})
export class EventTableRow {}
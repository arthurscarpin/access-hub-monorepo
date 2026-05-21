import { Component } from '@angular/core';
import { AccessEventsCardTableItem } from '../access-events-card-table-item/access-events-card-table-item';

@Component({
  selector: 'app-access-events-card-table',
  imports: [AccessEventsCardTableItem],
  templateUrl: './access-events-card-table.html',
})
export class AccessEventsCardTable {}

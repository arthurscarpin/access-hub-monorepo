import { Component } from '@angular/core';
import { AccessEventsCardPagination } from '../access-events-card-pagination/access-events-card-pagination';
import { AccessEventsCardSearch } from '../access-events-card-search/access-events-card-search';
import { AccessEventsCardTable } from '../access-events-card-table/access-events-card-table';

@Component({
  selector: 'app-access-events-card',
  standalone: true,
  imports: [AccessEventsCardPagination, AccessEventsCardSearch, AccessEventsCardTable],
  templateUrl: './access-events-card.html'
})
export class AccessEventsCard {}

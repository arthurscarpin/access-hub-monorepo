import { Component } from '@angular/core';
import { EventTabButton } from '../event-tab-button/event-tab-button';
import { InputSearch } from '../../../../shared/components/main/input-search/input-search';

@Component({
  selector: 'app-event-filters',
  imports: [EventTabButton, InputSearch],
  templateUrl: './event-filters.html',
  styleUrl: './event-filters.css',
})
export class EventFilters {}

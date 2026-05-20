import { Component } from '@angular/core';
import { TabButton } from '../tab-button/tab-button';
import { InputSearch } from '../../../../shared/components/content/input-search/input-search';

@Component({
  selector: 'app-event-filters',
  imports: [TabButton, InputSearch],
  templateUrl: './event-filters.html',
  styleUrl: './event-filters.css',
})
export class EventFilters {}

import { Component } from '@angular/core';
import { Header } from '../../../../shared/components/main/header/header';
import { EventPageTitle } from '../event-page-title/event-page-title';
import { EventPageActions } from '../event-page-actions/event-page-actions';
import { EventCard } from '../event-card/event-card';

@Component({
  selector: 'app-event-content',
  imports: [
    Header,
    EventPageTitle,
    EventPageActions,
    EventCard
  ],
  templateUrl: './event-content.html',
  styleUrl: './event-content.css',
})
export class EventContent { }
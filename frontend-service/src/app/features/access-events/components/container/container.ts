import { Component } from '@angular/core';
import { Header } from '../../../../shared/components/content/header/header';
import { PageTitle } from '../page-title/page-title';
import { PageActions } from '../page-actions/page-actions';
import { EventsCard } from '../events-card/events-card';

@Component({
  selector: 'app-container',
  imports: [
    Header,
    PageTitle,
    PageActions,
    EventsCard
  ],
  templateUrl: './container.html',
  styleUrl: './container.css',
})
export class Container { }
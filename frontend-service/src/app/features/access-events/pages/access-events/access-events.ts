import { Component } from '@angular/core';
import { SideMenu } from '../../../../shared/components/side-menu/side-menu';
import { TopMenu } from '../../../../shared/components/top-menu/top-menu';
import { PageTitle } from '../../../../shared/components/page-title/page-title';
import { AccessEventsCard } from '../../components/access-events-card/access-events-card';

@Component({
  selector: 'app-access-events',
  standalone: true,
  imports: [SideMenu, TopMenu, PageTitle, AccessEventsCard],
  templateUrl: './access-events.html',
})
export class AccessEvents {}
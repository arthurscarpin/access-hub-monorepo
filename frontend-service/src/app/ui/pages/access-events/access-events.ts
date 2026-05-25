import { Component } from '@angular/core';

import { Sidebar } from 'src/app/ui/components/shared/sidebar/sidebar';
import { Menu } from 'src/app/ui/components/shared/menu/menu';
import { PageHeader } from 'src/app/ui/components/shared/page-header/page-header';
import { AccessEventsContainer } from 'src/app/ui/components/access-events/access-events-container/access-events-container';

import { BREAD_CRUMB, PAGE_HEADER } from '@ui/pages/access-events/access-events.constants';


@Component({
  standalone: true,
  selector: 'app-access-events',
  imports: [Sidebar, Menu, PageHeader, AccessEventsContainer],
  templateUrl: './access-events.html',
})
export class AccessEvents {
  public readonly breadCrumb = BREAD_CRUMB;
  public readonly pageHeader = PAGE_HEADER;
}
import { Component } from '@angular/core';
import { SidebarContent } from '../../../../shared/components/menu/sidebar-content/sidebar-content';
import { EventContent } from '../../components/event-content/event-content';


@Component({
  selector: 'app-access-events',
  standalone: true,
  imports: [
    SidebarContent,
    EventContent
  ],
  templateUrl: './access-events.html',
})
export class AccessEvents { }
import { Component } from '@angular/core';
import { Sidebar } from '../../../../shared/components/sidebar/sidebar';
import { Container } from '../../components/container/container';


@Component({
  selector: 'app-access-events',
  standalone: true,
  imports: [
    Sidebar,
    Container
  ],
  templateUrl: './access-events.html',
})
export class AccessEvents { }
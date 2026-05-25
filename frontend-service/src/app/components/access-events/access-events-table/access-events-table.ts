import { Component, Input } from '@angular/core';
import { NgComponentOutlet, DatePipe } from '@angular/common';
import { AccessEventConfig } from '@components/shared/interfaces/ui.interfaces';

@Component({
  standalone: true,
  selector: 'app-access-events-table',
  imports: [NgComponentOutlet, DatePipe],
  templateUrl: './access-events-table.html'
})
export class AccessEventsTable {
  @Input() data: AccessEventConfig[] = [];
}

import { Component, Input, Type } from '@angular/core';
import { NgComponentOutlet, NgClass } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-event-history-item',
  imports: [NgComponentOutlet, NgClass],
  templateUrl: './event-history-item.html'
})
export class EventHistoryItem {
  @Input() plate: string = '';
  @Input() vehicle: string = '';
  @Input() status: 'granted' | 'denied' = 'granted';
  @Input() icon!: Type<any>;
}

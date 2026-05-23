import { Component, Input, Type } from '@angular/core';
import { NgComponentOutlet } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-event-card',
  imports: [NgComponentOutlet],
  templateUrl: './event-card.html'
})
export class EventCard {
  @Input() title: string = '';
  @Input() value: number = 0;
  @Input() percentage: string = '';
  @Input() description: string = '';
  @Input() icon!: Type<any>;
}

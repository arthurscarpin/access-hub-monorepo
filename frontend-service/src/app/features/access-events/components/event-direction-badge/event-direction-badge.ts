import { Component } from '@angular/core';
import { LucideAngularModule, ArrowDownLeft } from 'lucide-angular';

@Component({
  selector: 'app-event-direction-badge',
  imports: [LucideAngularModule],
  templateUrl: './event-direction-badge.html',
  styleUrl: './event-direction-badge.css',
})
export class EventDirectionBadge {
  readonly ArrowDownLeft = ArrowDownLeft;
}

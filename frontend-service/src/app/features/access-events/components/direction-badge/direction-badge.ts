import { Component } from '@angular/core';
import { LucideAngularModule, ArrowDownLeft } from 'lucide-angular';

@Component({
  selector: 'app-direction-badge',
  imports: [LucideAngularModule],
  templateUrl: './direction-badge.html',
  styleUrl: './direction-badge.css',
})
export class DirectionBadge {
  readonly ArrowDownLeft = ArrowDownLeft;
}

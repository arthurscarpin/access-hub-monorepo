import { Component, Input } from '@angular/core';
import { LucideBell } from '@lucide/angular';

@Component({
  standalone: true,
  selector: 'app-menu',
  imports: [LucideBell],
  templateUrl: './menu.html'
})
export class Menu {
  @Input() screenOperation!: string;
  @Input() screenName!: string;
}

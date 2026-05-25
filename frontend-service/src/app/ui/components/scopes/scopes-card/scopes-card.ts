import { Component, Input, Type } from '@angular/core';
import { NgComponentOutlet, NgClass } from '@angular/common';

@Component({
  standalone: true,
  imports: [NgComponentOutlet, NgClass],
  selector: 'app-scopes-card',
  templateUrl: './scopes-card.html'
})
export class ScopesCard {
  @Input() resource!: string;
  @Input() action!: string;
  @Input() description!: string;
  @Input() icon!: Type<any>;
}

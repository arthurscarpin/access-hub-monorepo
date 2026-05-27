import { Component, Input } from '@angular/core';
import { ScopePageConfig } from '../../pages/scopes-page/scope-page.config';
import { NgComponentOutlet, NgClass } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-scopes-card',
  imports: [NgComponentOutlet, NgClass],
  templateUrl: './scopes-card.html'
})
export class ScopesCard {
  @Input() config!: ScopePageConfig;
}

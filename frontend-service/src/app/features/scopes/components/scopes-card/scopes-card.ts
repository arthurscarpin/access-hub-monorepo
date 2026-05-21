import { Component } from '@angular/core';
import { LucideAngularModule, ShieldCheck } from 'lucide-angular';

@Component({
  selector: 'app-scopes-card',
  imports: [LucideAngularModule],
  templateUrl: './scopes-card.html'
})
export class ScopesCard {
  readonly ShieldCheck = ShieldCheck;
}

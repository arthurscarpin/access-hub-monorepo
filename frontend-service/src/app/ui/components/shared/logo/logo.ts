import { Component, input } from '@angular/core';
import { LucideShieldCheck } from '@lucide/angular';

@Component({
  standalone: true,
  selector: 'app-logo',
  imports: [LucideShieldCheck],
  templateUrl: './logo.html'
})
export class Logo {
  containerStyle = input<string>('');
  iconStyle = input<string>('');
}

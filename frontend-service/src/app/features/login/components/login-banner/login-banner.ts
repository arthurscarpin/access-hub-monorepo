import { Component } from '@angular/core';
import { LucideAngularModule, ShieldCheck} from 'lucide-angular';

@Component({
  selector: '[app-login-banner]',
  imports: [LucideAngularModule],
  templateUrl: './login-banner.html',
  styleUrl: './login-banner.css',
})
export class LoginBanner {
  readonly ShieldCheck = ShieldCheck;
}

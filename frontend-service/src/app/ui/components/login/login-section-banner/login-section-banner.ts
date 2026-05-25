import { Component } from '@angular/core';
import { Logo } from '@ui/components/shared/logo/logo';

@Component({
  standalone: true,
  selector: 'app-login-section-banner',
  imports: [Logo],
  templateUrl: './login-section-banner.html',
  styleUrl: './login-section-banner.css',
})
export class LoginSectionBanner {}

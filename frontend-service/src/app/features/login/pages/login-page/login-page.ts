import { Component } from '@angular/core';
import { LucideShieldCheck } from '@lucide/angular';
import { LoginCard } from '../../components/login-card/login-card';

@Component({
  standalone: true,
  selector: 'app-login-page',
  imports: [LucideShieldCheck, LoginCard],
  templateUrl: './login-page.html'
})
export class LoginPage {}

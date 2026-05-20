import { Component } from '@angular/core';

import {
  LucideAngularModule,
  ShieldCheck
} from 'lucide-angular';

@Component({
  selector: 'app-auth-layout',
  standalone: true,
  imports: [
    LucideAngularModule
  ],
  templateUrl: './auth-layout.html',
  styleUrl: './auth-layout.css',
})
export class AuthLayout {

  readonly ShieldCheck = ShieldCheck;

}
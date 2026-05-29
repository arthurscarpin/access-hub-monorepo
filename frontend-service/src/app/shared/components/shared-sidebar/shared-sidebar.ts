import { Component, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import {
  LucideShieldCheck,
  LucideLayoutDashboard,
  LucideActivity,
  LucideUpload,
  LucideCar,
  LucideCircleUser,
  LucideUsers,
  LucideShield,
  LucideLogOut,
} from '@lucide/angular';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  standalone: true,
  selector: 'app-shared-sidebar',
  imports: [
    LucideShieldCheck,
    LucideLayoutDashboard,
    LucideActivity,
    LucideUpload,
    LucideCar,
    LucideCircleUser,
    LucideUsers,
    LucideShield,
    LucideLogOut,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './shared-sidebar.html',
})
export class SharedSidebar {
  private readonly service = inject(AuthService);
  public readonly email = signal('');
  public readonly username = signal('');

  public ngOnInit(): void {
    const info = this.service.getLoginInfo();
    if (!info) return;
    this.email.set(info.email);
    this.username.set(info.username);
  }

  public logout(): void {
    this.service.logout();
  }
}

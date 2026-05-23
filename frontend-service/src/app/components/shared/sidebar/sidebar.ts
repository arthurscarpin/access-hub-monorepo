import { Component, OnInit } from '@angular/core';
import { NgComponentOutlet } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { LucideLogOut } from '@lucide/angular';
import { Logo } from '@components/shared/logo/logo';
import { SIDEBAR_MENU, SIDEBAR_ICON_MAP } from './sidebar.config';
import { AuthService } from '@core/services/auth.service';

@Component({
  standalone: true,
  selector: 'app-sidebar',
  imports: [
    RouterLink,
    RouterLinkActive,
    Logo,
    NgComponentOutlet,
    LucideLogOut
  ],
  templateUrl: './sidebar.html',
})
export class Sidebar implements OnInit {
  menu = SIDEBAR_MENU;
  icons = SIDEBAR_ICON_MAP;
  username: string = '';
  email: string = '';

  constructor(private authService: AuthService) {}
  
  ngOnInit(): void {
    ({ email: this.email, username: this.username} = this.authService.getUsernameAndEmail() || {});
  }

  logout(): void {
    this.authService.logout();
  }
}
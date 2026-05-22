import { Component } from '@angular/core';
import { Logo } from '@components/shared/logo/logo';
import { NgComponentOutlet } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { LucideLogOut } from '@lucide/angular';

import { SIDEBAR_MENU, SIDEBAR_ICON_MAP } from '../sidebar.config';

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
export class Sidebar {
  menu = SIDEBAR_MENU;
  icons = SIDEBAR_ICON_MAP;
}
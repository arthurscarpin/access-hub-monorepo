import { Component } from '@angular/core';
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

@Component({
  selector: 'app-side-menu',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    LucideShieldCheck,
    LucideLayoutDashboard,
    LucideActivity,
    LucideUpload,
    LucideCar,
    LucideCircleUser,
    LucideUsers,
    LucideShield,
    LucideLogOut,
  ],
  templateUrl: './side-menu.html',
})
export class SideMenu {}

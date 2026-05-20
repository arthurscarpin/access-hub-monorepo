import { Component } from '@angular/core';
import { SidebarLogo } from './sidebar-logo/sidebar-logo';
import { SidebarNavigation } from './sidebar-navigation/sidebar-navigation';
import { SidebarUserCard } from './sidebar-user-card/sidebar-user-card';


@Component({
  selector: 'app-sidebar',
  imports: [
    SidebarLogo,
    SidebarNavigation,
    SidebarUserCard
  ],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar { }

import { Component } from '@angular/core';
import { SidebarLogo } from '../../menu/sidebar-logo/sidebar-logo';
import { SidebarNavigation } from '../../menu/sidebar-navigation/sidebar-navigation';
import { SidebarUserCard } from '../../menu/sidebar-user-card/sidebar-user-card';


@Component({
  selector: 'app-sidebar',
  imports: [
    SidebarLogo,
    SidebarNavigation,
    SidebarUserCard
  ],
  templateUrl: './sidebar-content.html',
  styleUrl: './sidebar-content.css',
})
export class SidebarContent { }

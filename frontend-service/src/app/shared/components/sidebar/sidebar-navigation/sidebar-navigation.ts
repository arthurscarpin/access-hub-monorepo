import { Component } from '@angular/core';
import { SidebarSection } from '../sidebar-section/sidebar-section';

@Component({
  selector: 'app-sidebar-navigation',
  imports: [
    SidebarSection
  ],
  templateUrl: './sidebar-navigation.html',
  styleUrl: './sidebar-navigation.css',
})
export class SidebarNavigation { }
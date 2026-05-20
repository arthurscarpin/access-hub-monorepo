import { Component } from '@angular/core';
import { SidebarItem } from '../../menu/sidebar-item/sidebar-item';

@Component({
  selector: 'app-sidebar-section',
  imports: [
    SidebarItem
  ],
  templateUrl: './sidebar-section.html',
  styleUrl: './sidebar-section.css',
})
export class SidebarSection { }
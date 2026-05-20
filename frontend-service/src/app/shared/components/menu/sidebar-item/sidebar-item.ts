import { Component } from '@angular/core';
import { LucideAngularModule, LayoutDashboard } from 'lucide-angular';

@Component({
  selector: 'app-sidebar-item',
  imports: [
    LucideAngularModule
  ],
  templateUrl: './sidebar-item.html',
  styleUrl: './sidebar-item.css',
})
export class SidebarItem {
  readonly LayoutDashboard = LayoutDashboard;
}
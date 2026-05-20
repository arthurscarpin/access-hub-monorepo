import { Component } from '@angular/core';
import { LucideAngularModule, ShieldCheck} from 'lucide-angular';

@Component({
  selector: 'app-sidebar-logo',
  imports: [LucideAngularModule],
  templateUrl: './sidebar-logo.html',
  styleUrl: './sidebar-logo.css',
})
export class SidebarLogo {
  readonly ShieldCheck = ShieldCheck;
}
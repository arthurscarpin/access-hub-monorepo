import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  LucideAngularModule,
  Shield,
  LayoutDashboard,
  Activity,
  Camera,
  Car,
  Users,
  UserCog,
  ShieldCheck,
  LogOut,
  LayoutGrid,
  Search,
  Bell,
  Plus,
  MoreHorizontal
} from 'lucide-angular';

@Component({
  selector: 'app-users-page',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './users.html',
})
export class UsersControl {
  // Ícones da Sidebar
  readonly Shield = Shield;
  readonly LayoutDashboard = LayoutDashboard;
  readonly Activity = Activity;
  readonly Camera = Camera;
  readonly Car = Car;
  readonly Users = Users;
  readonly UserCog = UserCog;
  readonly ShieldCheck = ShieldCheck;
  readonly LogOut = LogOut;

  // Ícones do Header e Conteúdo
  readonly LayoutGrid = LayoutGrid;
  readonly Search = Search;
  readonly Bell = Bell;
  readonly Plus = Plus;
  
  // Ícones da Tabela
  readonly MoreHorizontal = MoreHorizontal;
}
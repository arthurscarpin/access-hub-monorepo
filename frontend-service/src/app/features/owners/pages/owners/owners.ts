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
  FileText,
  Mail,
  Ellipsis,
  Upload
} from 'lucide-angular';

@Component({
  selector: 'app-owners-page',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './owners.html',
})
export class Owners {
  readonly Shield = Shield;
  readonly LayoutDashboard = LayoutDashboard;
  readonly Activity = Activity;
  readonly Camera = Camera;
  readonly Car = Car;
  readonly Users = Users;
  readonly UserCog = UserCog;
  readonly ShieldCheck = ShieldCheck;
  readonly LogOut = LogOut;
  readonly LayoutGrid = LayoutGrid;
  readonly Search = Search;
  readonly Bell = Bell;
  readonly Plus = Plus;
  readonly FileText = FileText;
  readonly Mail = Mail;
  readonly Ellipsis = Ellipsis;
  readonly Upload = Upload;
}
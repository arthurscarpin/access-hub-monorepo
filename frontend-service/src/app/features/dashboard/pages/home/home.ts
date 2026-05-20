import { Component } from '@angular/core';
import {
  LucideAngularModule,
  LayoutDashboard,
  Car,
  Users,
  Shield,
  Activity,
  Search,
  Bell,
  ArrowUpRight,
  ArrowDownLeft,
  ShieldCheck,
  Clock3,
  Upload
} from 'lucide-angular';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [LucideAngularModule],
  templateUrl: './home.html',
})
export class Home {
  readonly LayoutDashboard = LayoutDashboard;
  readonly Car = Car;
  readonly Users = Users;
  readonly Shield = Shield;
  readonly Activity = Activity;
  readonly Search = Search;
  readonly Bell = Bell;
  readonly ArrowUpRight = ArrowUpRight;
  readonly ArrowDownLeft = ArrowDownLeft;
  readonly ShieldCheck = ShieldCheck;
  readonly Clock3 = Clock3;
  readonly Upload = Upload;
}
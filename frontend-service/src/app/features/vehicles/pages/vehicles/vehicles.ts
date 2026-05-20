import { Component } from '@angular/core';
import {
  LucideAngularModule,
  ShieldCheck,
  LayoutDashboard,
  Activity,
  Upload,
  Car,
  Users,
  Shield,
  Search,
  Bell,
  Plus,
  Ellipsis
} from 'lucide-angular';

@Component({
  selector: 'app-vehicles',
  standalone: true,
  imports: [LucideAngularModule],
  templateUrl: './vehicles.html',
})
export class Vehicles{
  readonly ShieldCheck = ShieldCheck;
  readonly LayoutDashboard = LayoutDashboard;
  readonly Activity = Activity;
  readonly Upload = Upload;
  readonly Car = Car;
  readonly Users = Users;
  readonly Shield = Shield;
  readonly Search = Search;
  readonly Bell = Bell;
  readonly Plus = Plus;
  readonly Ellipsis = Ellipsis;
}
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
  CloudUpload,
  FileArchive,
  ArrowDownLeft,
  ArrowUpRight
} from 'lucide-angular';

@Component({
  selector: 'app-captures',
  standalone: true,
  imports: [LucideAngularModule],
  templateUrl: './captures.html',
})
export class Captures {
  readonly ShieldCheck = ShieldCheck;
  readonly LayoutDashboard = LayoutDashboard;
  readonly Activity = Activity;
  readonly Upload = Upload;
  readonly Car = Car;
  readonly Users = Users;
  readonly Shield = Shield;
  readonly Search = Search;
  readonly Bell = Bell;
  readonly CloudUpload = CloudUpload;
  readonly FileArchive = FileArchive;
  readonly ArrowDownLeft = ArrowDownLeft;
  readonly ArrowUpRight = ArrowUpRight;
}
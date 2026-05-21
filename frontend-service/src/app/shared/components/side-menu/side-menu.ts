import { Component } from '@angular/core';
import { LucideAngularModule, ShieldCheck, LayoutDashboard, Activity, Upload, Car, Users, Shield } from 'lucide-angular';

@Component({
  selector: 'app-side-menu',
  imports: [LucideAngularModule],
  templateUrl: './side-menu.html'
})
export class SideMenu {
  readonly ShieldCheck = ShieldCheck;
  readonly LayoutDashboard = LayoutDashboard;
  readonly Activity = Activity;
  readonly Upload = Upload;
  readonly Car = Car;
  readonly Users = Users;
  readonly Shield = Shield;
}

import { Component } from '@angular/core';
import { LucideAngularModule, ShieldCheck, LayoutDashboard, Activity, Upload, Car, Users, Shield, CircleUserIcon, LogOut} from 'lucide-angular';
import { RouterLink, RouterLinkActive} from '@angular/router';

@Component({
  selector: 'app-side-menu',
  imports: [LucideAngularModule, RouterLink, RouterLinkActive],
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
  readonly CircleUserIcon = CircleUserIcon;
  readonly LogOut = LogOut;
}

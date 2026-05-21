import { Component } from '@angular/core';
import { LucideAngularModule, ShieldCheck, LayoutDashboard, Activity, Upload, Car, Users, Shield, CircleUserIcon, LogOut} from 'lucide-angular';
import { RouterLink, RouterLinkActive} from '@angular/router';

@Component({
  selector: 'app-side-menu',
  imports: [LucideAngularModule, RouterLink, RouterLinkActive],
  templateUrl: './side-menu.html'
})
export class SideMenu {
  readonly ShieldCheck: any = ShieldCheck;
  readonly LayoutDashboard: any = LayoutDashboard;
  readonly Activity: any = Activity;
  readonly Upload: any = Upload;
  readonly Car: any = Car;
  readonly Users: any = Users;
  readonly Shield: any = Shield;
  readonly CircleUserIcon: any = CircleUserIcon;
  readonly LogOut: any = LogOut;
}

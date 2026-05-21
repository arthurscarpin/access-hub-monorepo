import { Component } from '@angular/core';
import { TopMenu } from '../../../../shared/components/top-menu/top-menu';
import { SideMenu } from '../../../../shared/components/side-menu/side-menu';
import { PageTitle } from '../../../../shared/components/page-title/page-title';
import { ScopesCard } from '../../components/scopes-card/scopes-card';

import { 
  LucideAngularModule, 
  Lock, 
  Eye, 
  Wrench, 
  ShieldCheck, 
  Search, 
  ChevronRight, 
  LogOut, 
  UserCog, 
  Users, 
  Car, 
  Image, 
  Activity,
  LayoutDashboard
} from 'lucide-angular';

@Component({
  selector: 'app-scopes',
  imports: [LucideAngularModule, TopMenu, SideMenu, PageTitle, ScopesCard],
  templateUrl: './scopes.html'
})
export class Scopes {
  readonly Lock = Lock;
  readonly Eye = Eye;
  readonly Wrench = Wrench;
  readonly ShieldCheck = ShieldCheck;
  readonly Search = Search;
  readonly ChevronRight = ChevronRight;
  readonly LogOut = LogOut;
  readonly UserCog = UserCog;
  readonly Users = Users;
  readonly Car = Car;
  readonly Image = Image;
  readonly Activity = Activity;
  readonly LayoutDashboard = LayoutDashboard;
}

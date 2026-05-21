import { Component } from '@angular/core';
import { SideMenu } from '../../../../shared/components/side-menu/side-menu';
import { TopMenu } from '../../../../shared/components/top-menu/top-menu';
import { PageTitle } from '../../../../shared/components/page-title/page-title';
import { OwnersCard } from '../../components/owners-card/owners-card';

import { LucideAngularModule, Plus } from 'lucide-angular';

@Component({
  selector: 'app-owners-page',
  standalone: true,
  imports: [LucideAngularModule, SideMenu, TopMenu, PageTitle, OwnersCard],
  templateUrl: './owners.html',
})
export class Owners {
  readonly Plus: any = Plus;
}
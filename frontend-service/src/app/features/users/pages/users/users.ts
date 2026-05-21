import { Component } from '@angular/core';
import { SideMenu } from '../../../../shared/components/side-menu/side-menu';
import { TopMenu } from '../../../../shared/components/top-menu/top-menu';
import { PageTitle } from '../../../../shared/components/page-title/page-title';
import { UsersTable } from '../../components/users-table/users-table';

import { LucideAngularModule, Plus } from 'lucide-angular';

@Component({
  selector: 'app-users-page',
  standalone: true,
  imports: [LucideAngularModule, SideMenu, TopMenu, PageTitle, UsersTable],
  templateUrl: './users.html',
})
export class UsersControl {
  readonly Plus = Plus;
}
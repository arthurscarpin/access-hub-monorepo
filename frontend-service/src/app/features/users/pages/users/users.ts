import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { TopMenu } from '../../../../shared/components/top-menu/top-menu';
import { PageTitle } from '../../../../shared/components/page-title/page-title';
import { UsersTable } from '../../components/users-table/users-table';

@Component({
  selector: 'app-users-page',
  standalone: true,
  imports: [Sidebar, TopMenu, PageTitle, UsersTable],
  templateUrl: './users.html',
})
export class UsersControl {}
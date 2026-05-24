import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { UsersTable } from '../../components/users-table/users-table';

@Component({
  selector: 'app-users-page',
  standalone: true,
  imports: [Sidebar, UsersTable],
  templateUrl: './users.html',
})
export class UsersControl {}
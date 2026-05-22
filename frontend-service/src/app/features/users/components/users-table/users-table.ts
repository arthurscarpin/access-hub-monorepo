import { Component } from '@angular/core';
import { UsersTableItem } from '../users-table-item/users-table-item';

@Component({
  selector: 'app-users-table',
  standalone: true,
  imports: [UsersTableItem],
  templateUrl: './users-table.html'
})
export class UsersTable {}

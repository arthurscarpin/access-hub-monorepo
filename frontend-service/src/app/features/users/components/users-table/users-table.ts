import { Component } from '@angular/core';
import { UsersTableItem } from '../users-table-item/users-table-item';
import { LucideAngularModule, MoreHorizontal } from 'lucide-angular';

@Component({
  selector: 'app-users-table',
  imports: [LucideAngularModule, UsersTableItem],
  templateUrl: './users-table.html'
})
export class UsersTable {
  readonly MoreHorizontal = MoreHorizontal;
}

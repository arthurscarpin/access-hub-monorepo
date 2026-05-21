import { Component } from '@angular/core';
import { LucideAngularModule, MoreHorizontal } from 'lucide-angular';

@Component({
  selector: '[app-users-table-item]',
  imports: [LucideAngularModule],
  templateUrl: './users-table-item.html'
})
export class UsersTableItem {
  readonly MoreHorizontal = MoreHorizontal;
}

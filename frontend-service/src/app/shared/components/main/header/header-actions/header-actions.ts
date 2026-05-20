import { Component } from '@angular/core';
import { InputSearch } from '../../input-search/input-search';
import {
  LucideAngularModule,
  Search,
  Bell
} from 'lucide-angular';


@Component({
  selector: 'app-header-actions',
  imports: [
    LucideAngularModule,
    InputSearch
  ],
  templateUrl: './header-actions.html',
  styleUrl: './header-actions.css',
})
export class HeaderActions {
  readonly Search = Search;
  readonly Bell = Bell;
}
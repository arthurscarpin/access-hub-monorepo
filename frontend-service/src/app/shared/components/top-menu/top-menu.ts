import { Component } from '@angular/core';
import { InputSearch } from '../input-search/input-search';
import { LucideAngularModule, Bell } from 'lucide-angular';

@Component({
  selector: '[app-top-menu]',
  imports: [LucideAngularModule, InputSearch],
  templateUrl: './top-menu.html'
})
export class TopMenu {
  readonly Bell = Bell;
}

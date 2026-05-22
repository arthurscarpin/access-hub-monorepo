import { Component, Input } from '@angular/core';
import { InputSearch } from '../input-search/input-search';

import { LucideBell } from '@lucide/angular';

@Component({
  selector: '[app-top-menu]',
  standalone: true,
  imports: [InputSearch, LucideBell],
  templateUrl: './top-menu.html'
})
export class TopMenu {
  @Input() operation: string = '';
  @Input() page: string = '';
}
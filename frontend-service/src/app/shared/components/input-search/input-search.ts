import { Component } from '@angular/core';
import { LucideAngularModule, Search } from 'lucide-angular';

@Component({
  selector: 'app-input-search',
  imports: [
    LucideAngularModule,
  ],
  templateUrl: './input-search.html'
})
export class InputSearch {
  readonly Search: any = Search;
}

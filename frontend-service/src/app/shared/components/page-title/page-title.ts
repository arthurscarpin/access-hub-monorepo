import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-page-title',
  imports: [],
  templateUrl: './page-title.html'
})
export class PageTitle {
  @Input() category: string = '';
  @Input() title: string = '';
  @Input() description: string = '';
}

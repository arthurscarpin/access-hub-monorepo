import { Component, Input } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-page-header',
  templateUrl: './page-header.html'
})
export class PageHeader {
  @Input() category!: string;
  @Input() title!: string;
  @Input() description!: string;
}

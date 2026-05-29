import { Component, Input } from '@angular/core';
import { SharedHeaderConfig } from './shared-header-config';

@Component({
  standalone: true,
  selector: 'app-shared-header',
  imports: [],
  templateUrl: './shared-header.html'
})
export class SharedHeader {
  @Input() config!: SharedHeaderConfig;
}

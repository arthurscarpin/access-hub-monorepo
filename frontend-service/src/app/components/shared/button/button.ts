import { Component, Input } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-button',
  imports: [],
  templateUrl: './button.html'
})
export class Button {
  @Input() label: string = '';
  @Input() customStyle: string = '';
}
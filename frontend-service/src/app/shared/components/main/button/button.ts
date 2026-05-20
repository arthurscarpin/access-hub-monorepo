import { Component } from '@angular/core';
import { LucideAngularModule, Filter } from 'lucide-angular';

@Component({
  selector: 'app-button',
  imports: [
    LucideAngularModule
  ],
  templateUrl: './button.html',
  styleUrl: './button.css',
})
export class Button {
  readonly Filter = Filter;
}
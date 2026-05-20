import { Component } from '@angular/core';
import { HeaderActions } from './header-actions/header-actions';
import { HeaderBreadcrumb } from './header-breadcrumb/header-breadcrumb';

@Component({
  selector: 'app-header',
  imports: [
    HeaderActions,
    HeaderBreadcrumb
  ],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {}

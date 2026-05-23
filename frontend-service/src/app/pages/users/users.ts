import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { Menu } from '@components/shared/menu/menu';
import { PageHeader } from '@components/shared/page-header/page-header';
import { Button } from '@components/shared/button/button';

@Component({
  standalone: true,
  selector: 'app-users',
  imports: [Sidebar, Menu, PageHeader, Button],
  templateUrl: './users.html'
})
export class Users {
  // BreadCrumb
  breadCrumbOperation: string = 'Management';
  breadCrumbName: string = 'Users';
  
  // Page Header
  pageTitle: string = 'User';
  pageCategory: string = 'Management';
  pageDescription: string = 'Manage users and their respective scopes.';
  
  // Button
  buttonLabel: string = 'New User';
  buttonStyle: string = 'rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800 cursor-pointer';
}

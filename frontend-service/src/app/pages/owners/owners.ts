import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { Menu } from '@components/shared/menu/menu';
import { PageHeader } from '@components/shared/page-header/page-header';
import { Button } from '@components/shared/button/button';

@Component({
  standalone: true,
  selector: 'app-owners',
  imports: [Sidebar, Menu, PageHeader, Button],
  templateUrl: './owners.html'
})
export class Owners {
  // BreadCrumb
  breadCrumbOperation: string = 'Management';
  breadCrumbName: string = 'Owners';
  
  // Page Header
  pageTitle: string = 'Owners';
  pageCategory: string = 'Management';
  pageDescription: string = 'Register and view owner details.';
  
  // Button
  buttonLabel: string = 'New Owner';
  buttonStyle: string = 'rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800 cursor-pointer';
}

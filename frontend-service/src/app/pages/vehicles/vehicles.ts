import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { Menu } from '@components/shared/menu/menu';
import { PageHeader } from '@components/shared/page-header/page-header';
import { Button } from '@components/shared/button/button';

@Component({
  standalone: true,
  selector: 'app-vehicles',
  imports: [Sidebar, Menu, PageHeader, Button],
  templateUrl: './vehicles.html'
})
export class Vehicles {
  // BreadCrumb
  breadCrumbOperation: string = 'Management';
  breadCrumbName: string = 'Vehicles';
  
  // Page Header
  pageTitle: string = 'Vehicles';
  pageCategory: string = 'Management';
  pageDescription: string = 'Register and update the status of vehicles.';
  
  // Button
  buttonLabel: string = 'New Vehicle';
  buttonStyle: string = 'rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800 cursor-pointer';
}

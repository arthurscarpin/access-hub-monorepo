import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { Menu } from '@components/shared/menu/menu';
import { PageHeader } from '@components/shared/page-header/page-header';
import { Button } from '@components/shared/button/button';

@Component({
  standalone: true,
  selector: 'app-access-events',
  imports: [Sidebar, Menu, PageHeader, Button],
  templateUrl: './access-events.html'
})
export class AccessEvents {
  // BreadCrumb
  breadCrumbOperation: string = 'Operation';
  breadCrumbName: string = 'Access Events';
  
  // Page Header
  pageTitle: string = 'Access control';
  pageCategory: string = 'Operation';
  pageDescription: string = 'Complete history of recorded vehicle entries and exits.';
  
  // Button
  buttonLabel: string = 'Filter';
  buttonStyle: string = 'rounded-xl border border-slate-200 bg-white px-5 py-3 text-sm font-medium text-slate-700 transition hover:bg-slate-50 cursor-pointer';
}

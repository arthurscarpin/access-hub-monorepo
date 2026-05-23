import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { Menu } from '@components/shared/menu/menu';
import { PageHeader } from '@components/shared/page-header/page-header';
import { Button } from '@components/shared/button/button';
import { ACCESS_EVENTS_OPTIONS } from './access-events.options';
import { NgClass, NgComponentOutlet } from '@angular/common';


@Component({
  standalone: true,
  selector: 'app-access-events',
  imports: [NgClass, NgComponentOutlet, Sidebar, Menu, PageHeader, Button],
  templateUrl: './access-events.html',
})
export class AccessEvents {
  // BreadCrumb
  breadCrumbOperation: string = 'Operation';
  breadCrumbName: string = 'Access Events';

  // Page Header
  pageTitle: string = 'Access control';
  pageCategory: string = 'Operation';
  pageDescription: string = 'Complete history of recorded vehicle entries and exits.';

  // Pagination Buttons
  paginationButtons = [
    {
      label: 'Previous',
      style:
        'rounded-xl border border-slate-200 bg-white px-5 py-3 text-sm font-medium text-slate-700 transition hover:bg-slate-50 cursor-pointer',
    },
    {
      label: 'Next',
      style:
        'rounded-xl border border-slate-200 bg-white px-5 py-3 text-sm font-medium text-slate-700 transition hover:bg-slate-50 cursor-pointer',
    },
  ];

  // Filter Buttons
  filterButtons = [
    {
      label: 'All',
      style:
        'rounded-xl border border-slate-200 bg-white px-5 py-3 text-sm font-medium text-slate-700 transition hover:bg-slate-50 cursor-pointer',
    },
    {
      label: 'Granted',
      style:
        'rounded-xl border border-slate-200 bg-white px-5 py-3 text-sm font-medium text-slate-700 transition hover:bg-slate-50 cursor-pointer',
    },
    {
      label: 'Denied',
      style:
        'rounded-xl border border-slate-200 bg-white px-5 py-3 text-sm font-medium text-slate-700 transition hover:bg-slate-50 cursor-pointer',
    },
  ];

  selectedFilter: string = 'All';

  // Events
  events = ACCESS_EVENTS_OPTIONS;

  selectFilter(filter: string): void {
    this.selectedFilter = filter;
  }


  
}

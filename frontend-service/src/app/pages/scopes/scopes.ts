import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { Menu } from '@components/shared/menu/menu';
import { PageHeader } from '@components/shared/page-header/page-header';
import { ScopesCard } from '@components/scopes/scopes-card/scopes-card';
import { SCOPES } from '@pages/scopes/scopes.types';


@Component({
  standalone: true,
  selector: 'app-scopes',
  imports: [Sidebar, Menu, PageHeader, ScopesCard],
  templateUrl: './scopes.html'
})
export class Scopes {
  // BreadCrumb
  breadCrumbOperation: string = 'Management';
  breadCrumbName: string = 'Scopes';
  
  // Page Header
  pageTitle: string = 'Scopes';
  pageCategory: string = 'Management';
  pageDescription: string = 'Sets of permissions that can be assigned to platform users.';
  
  // Button
  buttonLabel: string = 'New capture';
  buttonStyle: string = 'rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800 cursor-pointer';

  // Scopes
  scopes = SCOPES;
}

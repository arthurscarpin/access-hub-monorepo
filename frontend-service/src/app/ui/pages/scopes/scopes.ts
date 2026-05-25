import { Component } from '@angular/core';
import { Sidebar } from 'src/app/ui/components/shared/sidebar/sidebar';
import { Menu } from 'src/app/ui/components/shared/menu/menu';
import { PageHeader } from 'src/app/ui/components/shared/page-header/page-header';
import { ScopesCard } from 'src/app/ui/components/scopes/scopes-card/scopes-card';
import { SCOPES } from '@ui/pages/scopes/scopes.types';


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

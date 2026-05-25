import { Component, OnInit, inject } from '@angular/core';
import { Sidebar } from 'src/app/ui/components/shared/sidebar/sidebar';
import { Menu } from 'src/app/ui/components/shared/menu/menu';
import { PageHeader } from 'src/app/ui/components/shared/page-header/page-header';
import { ScopesCard } from 'src/app/ui/components/scopes/scopes-card/scopes-card';
import { ScopeService } from '@core/services/scope.service';
import { BREAD_CRUMB, PAGE_HEADER } from './scopes.contants';


@Component({
  standalone: true,
  selector: 'app-scopes',
  imports: [Sidebar, Menu, PageHeader, ScopesCard],
  templateUrl: './scopes.html'
})
export class Scopes implements OnInit {
  private scopeService = inject(ScopeService);

  public scopes = this.scopeService.scopes;
  public readonly breadCrumb = BREAD_CRUMB;
  public readonly pageHeader = PAGE_HEADER;
  
  ngOnInit(): void {
    this.scopeService.load();
  }
}

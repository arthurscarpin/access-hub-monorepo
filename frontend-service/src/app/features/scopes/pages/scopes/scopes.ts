import { Component, OnInit, inject } from '@angular/core';

import { ScopeService } from '../../../../core/services/scope.service';

import { TopMenu } from '../../../../shared/components/top-menu/top-menu';
import { SideMenu } from '../../../../shared/components/side-menu/side-menu';
import { PageTitle } from '../../../../shared/components/page-title/page-title';

import { ScopesCard } from '../../components/scopes-card/scopes-card';

export interface ScopeResponse {
  id: string,
  name: string;
}

@Component({
  selector: 'app-scopes',
  standalone: true,
  imports: [TopMenu, SideMenu, PageTitle, ScopesCard],
  templateUrl: './scopes.html',
})
export class Scopes implements OnInit {
  private scopeService = inject(ScopeService);

  scopes: ScopeResponse[] = [];

  ngOnInit(): void {
    this.loadScopes();
  }

  loadScopes(): void {
    this.scopeService.getScopes().subscribe({
      next: (response) => {
        console.log('SCOPES RESPONSE', response);

        this.scopes = response;
      },

      error: (err) => {
        console.log('SCOPES ERROR');

        console.log(err);
      },
    });
  }
}

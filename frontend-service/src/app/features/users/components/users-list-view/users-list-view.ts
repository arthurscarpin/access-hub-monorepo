import { Component, inject, computed } from '@angular/core';
import { UserService } from '../../../../core/services/users.service';
import { ScopeService } from '../../../../core/services/scope.service';
import { UserConfig } from './user-list.config';
import { Scope } from '../../../../core/models/scope.model';
import { NgClass } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-users-list-view',
  imports: [NgClass],
  templateUrl: './users-list-view.html',
})
export class UsersListView {
  private readonly scopeService = inject(ScopeService);
  private readonly userService = inject(UserService);

  public readonly scopes = this.scopeService.scopes;

  public readonly users = this.userService.users;
  public readonly loading = this.userService.loading;
  public readonly totalElements = this.userService.totalElements;
  public readonly pagination = this.userService.pagination;

  private scopePriority(scopeName: string): number {
    const action = scopeName.split(':')[1];

    if (action === 'all') return 0;
    if (action === 'write') return 1;
    if (action === 'read') return 2;

    return 99;
  }

  public readonly usersWithScopes = computed<UserConfig[]>(() => {
    const scopesMap = new Map(this.scopes().map((scope) => [scope.id, scope]));

    return this.users().map((user) => ({
      ...user,
      scopes: user.scopes
        .map((scopeId) => scopesMap.get(scopeId))
        .filter((scope): scope is Scope => !!scope)
        .sort((a, b) => {
          return this.scopePriority(a.name) - this.scopePriority(b.name);
        }),
    }));
  });

  public ngOnInit(): void {
    this.scopeService.findAll();
    this.userService.findAll();
  }

  public changePage(action: 'next' | 'previous'): void {
    const pagination = this.pagination();

    if (!pagination) return;

    if (action === 'next' && pagination.last) return;
    if (action === 'previous' && pagination.first) return;

    const nextPage = action === 'next' ? pagination.number + 1 : pagination.number - 1;

    this.userService.findAll(nextPage);
  }
}

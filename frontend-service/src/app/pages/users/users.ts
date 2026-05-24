import { Component, signal } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { Menu } from '@components/shared/menu/menu';
import { PageHeader } from '@components/shared/page-header/page-header';
import { Button } from '@components/shared/button/button';
import { Table } from '@components/shared/table/table';
import { TableConfig } from '@components/shared/table/table.interface';
import { USERS_OPTIONS } from '@pages/users/users.types';
import { SCOPES } from '@pages/scopes/scopes.types';

type UserRow = (typeof USERS_OPTIONS)[number];

@Component({
  standalone: true,
  selector: 'app-users',
  imports: [Sidebar, Menu, PageHeader, Button, Table],
  templateUrl: './users.html',
})
export class Users {
  isCreateUserOpen = signal(false);

  name = signal('');
  email = signal('');
  selectedScopes = signal<string[]>([]);

  error = signal('');

  breadCrumbOperation: string = 'Management';
  breadCrumbName: string = 'Users';

  pageTitle: string = 'User';
  pageCategory: string = 'Management';
  pageDescription: string = 'Manage users and their respective scopes.';

  buttonLabel: string = 'New User';
  buttonStyle: string =
    'rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800 cursor-pointer';

  users: UserRow[] = USERS_OPTIONS;

  scopes = SCOPES;

  tableConfig: TableConfig<UserRow> = {
    columns: [
      {
        key: 'name',
        label: 'Name',
        render: (row) => `
          <span class="text-sm font-medium text-slate-800">${row.name}</span>
        `,
      },
      {
        key: 'email',
        label: 'Email',
        render: (row) => `
          <span class="text-sm text-slate-500">${row.email}</span>
        `,
      },
      {
        key: 'scopes',
        label: 'Scopes',
        render: (row) => `
          <div class="flex flex-wrap gap-1.5">
            ${row.scopes
              .map((scope) => {
                const [resource, action] = scope.split(':');

                const formattedResource =
                  resource.charAt(0).toUpperCase() + resource.slice(1).replaceAll('_', ' ');

                const formattedAction =
                  action.charAt(0).toUpperCase() + action.slice(1);

                const style =
                  action === 'all'
                    ? 'border-blue-200 bg-blue-50 text-blue-700'
                    : action === 'write'
                      ? 'border-orange-200 bg-orange-50 text-orange-700'
                      : action === 'read'
                        ? 'border-emerald-200 bg-emerald-50 text-emerald-700'
                        : 'border-slate-200 bg-slate-50 text-slate-600';

                return `
                  <span class="px-2.5 py-1 text-xs font-medium rounded-full border ${style}">
                    ${formattedResource}: ${formattedAction}
                  </span>
                `;
              })
              .join('')}
          </div>
        `,
      },
    ],
  };

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

  openCreateUserModal() {
    this.resetForm();
    this.isCreateUserOpen.set(true);
  }

  closeCreateUserModal() {
    this.isCreateUserOpen.set(false);
    this.error.set('');
  }

  resetForm() {
    this.name.set('');
    this.email.set('');
    this.selectedScopes.set([]);
    this.error.set('');
  }

  setName(value: string) {
    this.name.set(value);
  }

  setEmail(value: string) {
    this.email.set(value);
  }

  toggleScope(value: string, checked: boolean) {
    this.selectedScopes.update((current) => {
      if (checked) return [...current, value];
      return current.filter((v) => v !== value);
    });
  }

  createUser() {
    if (!this.name().trim()) {
      this.error.set('Name is required');
      return;
    }

    if (!this.email().trim()) {
      this.error.set('Email is required');
      return;
    }

    if (!this.email().includes('@')) {
      this.error.set('Invalid email');
      return;
    }

    if (this.selectedScopes().length === 0) {
      this.error.set('At least one scope is required');
      return;
    }

    this.error.set('');
    this.closeCreateUserModal();
  }
}
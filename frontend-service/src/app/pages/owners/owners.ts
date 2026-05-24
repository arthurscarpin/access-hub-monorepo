import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { Menu } from '@components/shared/menu/menu';
import { PageHeader } from '@components/shared/page-header/page-header';
import { Button } from '@components/shared/button/button';
import { Table } from '@components/shared/table/table';
import { TableConfig } from '@components/shared/table/table.interface';
import { OWNERS_OPTIONS } from '@pages/owners/owners.types';

type OwnerRow = (typeof OWNERS_OPTIONS)[number];

@Component({
  standalone: true,
  selector: 'app-owners',
  imports: [Sidebar, Menu, PageHeader, Button, Table],
  templateUrl: './owners.html',
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
  buttonStyle: string =
    'rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800 cursor-pointer';
  

  // Table
  owners: OwnerRow[] = OWNERS_OPTIONS;

  tableConfig: TableConfig<OwnerRow> = {
    columns: [
      {
        key: 'name',
        label: 'Name',
        render: (row) => `
        <span class="text-sm font-medium text-slate-800">
          ${row.name}
        </span>
      `,
      },

      {
        key: 'email',
        label: 'Email',
        render: (row) => `
        <span class="text-sm text-slate-500">
          ${row.email}
        </span>
      `,
      },

      {
        key: 'documentType',
        label: 'Document Type',
        render: (row) => `
        <span class="text-sm text-slate-500">
          ${row.documentType}
        </span>
      `,
      },

      {
        key: 'document',
        label: 'Document',
        render: (row) => `
        <span class="text-sm text-slate-500">
          ${row.document}
        </span>
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
}

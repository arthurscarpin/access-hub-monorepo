import { Component } from '@angular/core';

import { Sidebar } from '@components/shared/sidebar/sidebar';
import { Menu } from '@components/shared/menu/menu';
import { PageHeader } from '@components/shared/page-header/page-header';
import { Button } from '@components/shared/button/button';
import { Table } from '@components/shared/table/table';
import { TableConfig } from '@components/shared/table/table.interface';
import { ACCESS_EVENTS_OPTIONS } from '@pages/access-events/access-events.types';

type EventRow = (typeof ACCESS_EVENTS_OPTIONS)[number];

@Component({
  standalone: true,
  selector: 'app-access-events',
  imports: [Sidebar, Menu, PageHeader, Button, Table],
  templateUrl: './access-events.html',
})
export class AccessEvents {
  // BreadCrumb
  breadCrumbOperation = 'Operation';
  breadCrumbName = 'Access Events';

  // Page Header
  pageTitle = 'Access control';
  pageCategory = 'Operation';
  pageDescription = 'Complete history of recorded vehicle entries and exits.';

  events: EventRow[] = ACCESS_EVENTS_OPTIONS;

  tableConfig: TableConfig<EventRow> = {
    columns: [
      {
        key: 'plate',
        label: 'Plate',
        render: (row) => `
        <span class="text-sm font-semibold text-slate-900 tracking-wide">
          ${row.plate}
        </span>
      `,
      },

      {
        key: 'direction',
        label: 'Direction',
        render: (row) => `
        <div class="flex items-center gap-2 text-sm font-medium ${
          row.direction === 'Entry' ? 'text-emerald-600' : 'text-red-600'
        }">
          ${row.direction}
        </div>
      `,
      },

      {
        key: 'status',
        label: 'Status',
        render: (row) => `
        <span class="px-3 py-1 rounded-full text-xs ${
          row.status === 'Granted' ? 'bg-emerald-50 text-emerald-700' : 'bg-red-50 text-red-700'
        }">
          ${row.status}
        </span>
      `,
      },

      {
        key: 'date',
        label: 'Date',
        render: (row) => `
        <span class="text-sm text-slate-400">
          ${new Date(row.date).toLocaleString('en-US', {
            dateStyle: 'short',
            timeStyle: 'short',
          })}
        </span>
      `,
      },
    ],
  };

  filters = [
    {
      label: 'All',
      style: 'rounded-xl border border-slate-200 px-5 py-3 text-sm cursor-pointer',
    },
    {
      label: 'Granted',
      style: 'rounded-xl border border-slate-200 px-5 py-3 text-sm cursor-pointer',
    },
    {
      label: 'Denied',
      style: 'rounded-xl border border-slate-200 px-5 py-3 text-sm cursor-pointer',
    },
    {
      label: 'Entry',
      style: 'rounded-xl border border-slate-200 px-5 py-3 text-sm cursor-pointer',
    },
    {
      label: 'Exit',
      style: 'rounded-xl border border-slate-200 px-5 py-3 text-sm cursor-pointer',
    },
  ];

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

  selectedFilter = 'All';
  selectFilter(filter: string) {
    this.selectedFilter = filter;
  }
}

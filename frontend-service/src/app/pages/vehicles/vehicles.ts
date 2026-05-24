import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { Menu } from '@components/shared/menu/menu';
import { PageHeader } from '@components/shared/page-header/page-header';
import { Button } from '@components/shared/button/button';
import { Table } from '@components/shared/table/table';
import { TableConfig } from '@components/shared/table/table.interface';
import { VEHICLES_OPTIONS } from '@pages/vehicles/vehicles.types';

type VehicleRow = (typeof VEHICLES_OPTIONS)[number];

@Component({
  standalone: true,
  selector: 'app-vehicles',
  imports: [Sidebar, Menu, PageHeader, Button, Table],
  templateUrl: './vehicles.html',
})
export class Vehicles {
  // BreadCrumb
  breadCrumbOperation: string = 'Management';
  breadCrumbName: string = 'Vehicles';

  // Page Header
  pageTitle: string = 'Vehicles';
  pageCategory: string = 'Management';
  pageDescription: string = 'Register and update the status of vehicles.';

  // Button
  buttonLabel: string = 'New Vehicle';
  buttonStyle: string =
    'rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800 cursor-pointer';

  // Table
  vehicles: VehicleRow[] = VEHICLES_OPTIONS;

  tableConfig: TableConfig<VehicleRow> = {
    columns: [
      {
        key: 'plate',
        label: 'Plate',
        render: (row) => `
        <span class="text-sm font-medium text-slate-800">
          ${row.plate}
        </span>
      `,
      },
      {
        key: 'model',
        label: 'Model',
        render: (row) => `
        <span class="text-sm text-slate-500">
          ${row.model}
        </span>
      `,
      },
      {
        key: 'status',
        label: 'Status',
        render: (row) => `
        <span class="px-3 py-1 rounded-full text-xs ${
          row.status === 'ACTIVE' ? 'bg-emerald-50 text-emerald-700' : 'bg-red-50 text-red-700'
        }">
          ${row.status.charAt(0) + row.status.slice(1).toLowerCase()}
        </span>
      `,
      },
      {
        key: 'ownerName',
        label: 'Owner',
        render: (row) => `
        <span class="text-sm text-slate-500">
          ${row.ownerName}
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

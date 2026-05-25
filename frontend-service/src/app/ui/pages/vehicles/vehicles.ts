import { Component, signal } from '@angular/core';
import { Sidebar } from 'src/app/ui/components/shared/sidebar/sidebar';
import { Menu } from 'src/app/ui/components/shared/menu/menu';
import { PageHeader } from 'src/app/ui/components/shared/page-header/page-header';
import { Button } from 'src/app/ui/components/shared/button/button';
import { Table } from 'src/app/ui/components/shared/table/table';
import { TableConfig } from 'src/app/ui/components/shared/table/table.interface';
import { VEHICLES_OPTIONS } from '@ui/pages/vehicles/vehicles.types';

type VehicleRow = (typeof VEHICLES_OPTIONS)[number];

@Component({
  standalone: true,
  selector: 'app-vehicles',
  imports: [Sidebar, Menu, PageHeader, Button, Table],
  templateUrl: './vehicles.html',
})
export class Vehicles {
  isCreateVehicleOpen = signal(false);

  plate = signal('');
  model = signal('');
  ownerName = signal('');
  error = signal('');

  breadCrumbOperation: string = 'Management';
  breadCrumbName: string = 'Vehicles';

  pageTitle: string = 'Vehicles';
  pageCategory: string = 'Management';
  pageDescription: string = 'Register and update the status of vehicles.';

  buttonLabel: string = 'New Vehicle';
  buttonStyle: string =
    'rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800 cursor-pointer';

  vehicles: VehicleRow[] = VEHICLES_OPTIONS;

  tableConfig: TableConfig<VehicleRow> = {
    columns: [
      {
        key: 'plate',
        label: 'Plate',
        render: (row) => `
          <span class="text-sm font-medium text-slate-800">${row.plate}</span>
        `,
      },
      {
        key: 'model',
        label: 'Model',
        render: (row) => `
          <span class="text-sm text-slate-500">${row.model}</span>
        `,
      },
      {
        key: 'status',
        label: 'Status',
        render: (row) => `
          <span class="px-3 py-1 rounded-full text-xs ${
            row.status === 'ACTIVE'
              ? 'bg-emerald-50 text-emerald-700'
              : 'bg-red-50 text-red-700'
          }">
            ${row.status.charAt(0) + row.status.slice(1).toLowerCase()}
          </span>
        `,
      },
      {
        key: 'ownerName',
        label: 'Owner',
        render: (row) => `
          <span class="text-sm text-slate-500">${row.ownerName}</span>
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

  openCreateVehicleModal() {
    this.resetForm();
    this.isCreateVehicleOpen.set(true);
  }

  closeCreateVehicleModal() {
    this.isCreateVehicleOpen.set(false);
    this.error.set('');
  }

  resetForm() {
    this.plate.set('');
    this.model.set('');
    this.ownerName.set('');
    this.error.set('');
  }

  setPlate(value: string) {
    this.plate.set(value);
  }

  setModel(value: string) {
    this.model.set(value);
  }

  setOwnerName(value: string) {
    this.ownerName.set(value);
  }

  createVehicle() {
    if (!this.plate().trim()) {
      this.error.set('Plate is required');
      return;
    }

    if (!this.model().trim()) {
      this.error.set('Model is required');
      return;
    }

    if (!this.ownerName().trim()) {
      this.error.set('Owner is required');
      return;
    }

    this.error.set('');
    this.closeCreateVehicleModal();
  }
}
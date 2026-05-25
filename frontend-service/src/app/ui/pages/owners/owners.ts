import { Component, signal } from '@angular/core';
import { Sidebar } from 'src/app/ui/components/shared/sidebar/sidebar';
import { Menu } from 'src/app/ui/components/shared/menu/menu';
import { PageHeader } from 'src/app/ui/components/shared/page-header/page-header';
import { Button } from 'src/app/ui/components/shared/button/button';
import { Table } from 'src/app/ui/components/shared/table/table';
import { TableConfig } from 'src/app/ui/components/shared/table/table.interface';
import { OWNERS_OPTIONS } from '@ui/pages/owners/owners.types';

type OwnerRow = (typeof OWNERS_OPTIONS)[number];

@Component({
  standalone: true,
  selector: 'app-owners',
  imports: [Sidebar, Menu, PageHeader, Button, Table],
  templateUrl: './owners.html',
})
export class Owners {
  isCreateOwnerOpen = signal(false);

  name = signal('');
  email = signal('');
  documentType = signal('');
  document = signal('');
  error = signal('');

  breadCrumbOperation: string = 'Management';
  breadCrumbName: string = 'Owners';

  pageTitle: string = 'Owners';
  pageCategory: string = 'Management';
  pageDescription: string = 'Register and view owner details.';

  buttonLabel: string = 'New Owner';
  buttonStyle: string =
    'rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800 cursor-pointer';

  owners: OwnerRow[] = OWNERS_OPTIONS;

  tableConfig: TableConfig<OwnerRow> = {
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
        key: 'documentType',
        label: 'Document Type',
        render: (row) => `
          <span class="text-sm text-slate-500">${row.documentType}</span>
        `,
      },
      {
        key: 'document',
        label: 'Document',
        render: (row) => `
          <span class="text-sm text-slate-500">${row.document}</span>
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

  openCreateOwnerModal() {
    this.resetForm();
    this.isCreateOwnerOpen.set(true);
  }

  closeCreateOwnerModal() {
    this.isCreateOwnerOpen.set(false);
    this.error.set('');
  }

  resetForm() {
    this.name.set('');
    this.email.set('');
    this.documentType.set('');
    this.document.set('');
    this.error.set('');
  }

  setName(value: string) {
    this.name.set(value);
  }

  setEmail(value: string) {
    this.email.set(value);
  }

  setDocumentType(value: string) {
    this.documentType.set(value);
  }

  setDocument(value: string) {
    this.document.set(value);
  }

  createOwner() {
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

    if (!this.documentType().trim()) {
      this.error.set('Document type is required');
      return;
    }

    if (!this.document().trim()) {
      this.error.set('Document is required');
      return;
    }

    this.error.set('');
    this.closeCreateOwnerModal();
  }
}
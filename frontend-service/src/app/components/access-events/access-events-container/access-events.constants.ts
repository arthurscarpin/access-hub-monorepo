import { TableConfig } from "@components/shared/interfaces/ui.interfaces";

export const ACCESS_EVENTS_TABLE_CONFIG: TableConfig<any> = {
  columns: [
    {
      key: 'plate',
      label: 'Plate',
      render: (row) => `
        <span class="text-sm font-semibold text-slate-900 tracking-wide">${row.plate}</span>
      `,
    },
    {
      key: 'direction',
      label: 'Direction',
      render: (row) => `
        <div class="flex items-center gap-2 text-sm font-medium ${
          row.direction === 'Entry' ? 'text-emerald-600' : 'text-red-600'
        }">${row.direction}</div>
      `,
    },
    {
      key: 'status',
      label: 'Status',
      render: (row) => `
        <span class="px-3 py-1 rounded-full text-xs ${
          row.status === 'Granted' ? 'bg-emerald-50 text-emerald-700' : 'bg-red-50 text-red-700'
        }">${row.status}</span>
      `,
    },
    {
      key: 'date',
      label: 'Date',
      render: (row) => `
        <span class="text-sm text-slate-400">
          ${new Date(row.date).toLocaleString('en-US', { dateStyle: 'short', timeStyle: 'short' })}
        </span>
      `,
    },
  ],
};

export const FILTER_OPTIONS = ['All', 'Granted', 'Denied', 'Entry', 'Exit'];

export const PAGINATION_BUTTONS = [
  { label: 'Previous', action: 'prev' },
  { label: 'Next', action: 'next' },
];
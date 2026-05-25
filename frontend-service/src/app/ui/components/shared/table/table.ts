import { Component, Input } from '@angular/core';
import { TableConfig } from '@ui/components/shared/table/table.interface';

@Component({
  standalone: true,
  selector: 'app-table',
  templateUrl: './table.html',
})
export class Table<T = any> {
  @Input() data: T[] = [];
  @Input() tableConfig!: TableConfig<T>;
}

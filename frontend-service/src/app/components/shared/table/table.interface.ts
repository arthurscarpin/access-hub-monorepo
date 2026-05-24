export interface TableColumn<T = any> {
  key: keyof T;
  label: string;
  render?: (row: T) => string;
}

export interface TableConfig<T = any> {
  columns: TableColumn<T>[];
}
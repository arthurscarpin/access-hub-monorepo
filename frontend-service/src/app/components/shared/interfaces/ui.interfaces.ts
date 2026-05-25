import { Type } from "@angular/core";

// Bread Crumb Interface
export interface BreadCrumbConfig {
  operation: string;
  name: string;
}

// Page Header Interface
export interface PageHeaderConfig {
  title: string;
  category: string;
  description: string;
}

// Access Event Table Config
export interface AccessEventConfig {
  id: string;
  plate: string;
  direction: 'Entry' | 'Exit';
  status: 'Granted' | 'Denied';
  date: string;
  icon: Type<any>;
}

// Generic Table Config Interface
export interface TableColumn<T = any> {
  key: keyof T;
  label: string;
  render?: (row: T) => string;
}

export interface TableConfig<T = any> {
  columns: TableColumn<T>[];
}
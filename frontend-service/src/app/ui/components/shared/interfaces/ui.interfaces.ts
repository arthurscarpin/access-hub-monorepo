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
  direction: string;
  status: string;
  date: string;
  icon: Type<any>;
}

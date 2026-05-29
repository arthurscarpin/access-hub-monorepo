import { Type } from "@angular/core";

export interface DashboardCardConfig {
  title: string;
  value: number;
  description: string;
  icon: Type<any>;
  color: string;
}
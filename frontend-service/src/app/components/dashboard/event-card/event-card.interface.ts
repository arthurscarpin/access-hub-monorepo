import { Type } from '@angular/core';

export interface EventCard {
  title: string;
  value: number;
  percentage: string;
  description: string;
  icon: Type<any>;
}

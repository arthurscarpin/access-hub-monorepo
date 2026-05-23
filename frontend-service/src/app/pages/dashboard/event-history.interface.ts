import { Type } from '@angular/core';

export interface EventHistoryItem {
  plate: string;
  vehicle: string;
  status: 'granted' | 'denied';
  icon: Type<any>;
}
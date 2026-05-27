import { Type } from '@angular/core';

export interface ScopePageConfig {
  id: string;
  resource: string;
  action: string;
  description: string;
  icon: Type<any>;
}

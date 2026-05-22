import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

export interface ScopeResponse {
  name: string;
}

@Component({
  selector: 'app-scopes-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './scopes-card.html',
})
export class ScopesCard {
  @Input({ required: true }) scope!: ScopeResponse;

  get resource(): string {
    return this.scope?.name?.split(':')[0] ?? '';
  }

  get action(): string {
    return this.scope?.name?.split(':')[1] ?? '';
  }

  get actionColor(): string {
    const colors: Record<string, string> = {
      read: 'text-blue-600 bg-blue-50 border-blue-100',
      write: 'text-amber-600 bg-amber-50 border-amber-100',
      all: 'text-emerald-600 bg-emerald-50 border-emerald-100',
    };

    return colors[this.action] || 'text-slate-600 bg-slate-50 border-slate-100';
  }

  get description(): string {
    const descriptions: Record<string, string> = {
      admin: 'Full system administration access',
      vehicle: 'Vehicle management permissions',
      owner: 'Owner management permissions',
      access_event: 'Access event monitoring permissions',
      capture: 'Capture processing permissions',
    };

    return descriptions[this.resource] || 'System permission';
  }
}
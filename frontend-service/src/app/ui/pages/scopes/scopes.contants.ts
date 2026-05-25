import { Type } from '@angular/core';
import { LucideShieldCheck, LucideBookOpen, LucidePencilLine } from '@lucide/angular';
import { BreadCrumbConfig, PageHeaderConfig } from "src/app/ui/components/shared/interfaces/ui.interfaces";

export interface ScopeConfig {
  icon: Type<any>;
  description: string;
}

export const SCOPES_MAPPER: Record<string, ScopeConfig> = {
  'read': {
    icon: LucideBookOpen,
    description: 'Read access to the resource',
  },
  'write': {
    icon: LucidePencilLine,
    description: 'Write access to the resource',
  },
  'all': {
    icon: LucideShieldCheck,
    description: 'Full access to the platform and settings.',
  }
};

export const BREAD_CRUMB: BreadCrumbConfig = {
  operation: 'Operation',
  name: 'Access Events',
};

export const PAGE_HEADER: PageHeaderConfig = {
  title: 'Access control',
  category: 'Operation',
  description: 'Complete history of recorded vehicle entries and exits.'
};

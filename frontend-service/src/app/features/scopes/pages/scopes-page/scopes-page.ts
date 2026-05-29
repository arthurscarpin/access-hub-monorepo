import { Component, computed, inject } from '@angular/core';
import { SharedSidebar } from '../../../../shared/components/shared-sidebar/shared-sidebar';
import { SharedMenu } from '../../../../shared/components/shared-menu/shared-menu';
import { SharedHeader } from '../../../../shared/components/shared-header/shared-header';
import { SharedHeaderConfig } from '../../../../shared/components/shared-header/shared-header-config';
import { SharedMenuConfig } from '../../../../shared/components/shared-menu/shared-menu.config';
import { ScopesCard } from '../../components/scopes-card/scopes-card';
import { ScopeService } from '../../../../core/services/scope.service';
import { ScopeMapper } from './scopes-page.mapper';
import { LucideShieldCheck, LucideBookOpen, LucidePencilLine } from '@lucide/angular';

const scopesMapper: Record<string, ScopeMapper> = {
  read: {
    icon: LucideBookOpen,
    description: 'Read access to the resource',
  },
  write: {
    icon: LucidePencilLine,
    description: 'Write access to the resource',
  },
  all: {
    icon: LucideShieldCheck,
    description: 'Full access to the platform and settings.',
  },
};

@Component({
  standalone: true,
  selector: 'app-scopes-page',
  imports: [SharedSidebar, SharedMenu, SharedHeader, ScopesCard],
  templateUrl: './scopes-page.html',
})
export class ScopesPage {
  private readonly service = inject(ScopeService);

  public readonly headerConfig: SharedHeaderConfig = {
    category: 'Management',
    title: 'Scopes',
    description: 'Control panel for permitted scopes for users',
  };

  public readonly menuConfig: SharedMenuConfig = {
    category: 'Management',
    title: 'Scopes',
  };

  public readonly scopes = this.service.scopes;

  public readonly scopesMapped = computed(() => {
    return this.scopes().map((scope) => {
      const [resourceKey, actionKey] = scope.name.split(':');
      const action = scopesMapper[actionKey] || scopesMapper['read'];
      const resource = resourceKey.replace(/_/g, ' ');
      return {
        id: scope.id,
        resource: resource,
        action: actionKey || 'all',
        description: action.description,
        icon: action.icon,
      };
    });
  });

  public ngOnInit(): void {
    this.service.findAll();
  }
}

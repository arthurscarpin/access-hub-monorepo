import { Component, signal, inject } from '@angular/core';
import { OwnersRegisterModal } from '../../components/owners-register-modal/owners-register-modal';
import { OwnersListView } from '../../components/owners-list-view/owners-list-view';
import { OwnerService } from '../../../../core/services/owner.service';
import { Owner } from '../../../../core/models/owner.model';
import { SharedSidebar } from '../../../../shared/components/shared-sidebar/shared-sidebar';
import { SharedMenu } from '../../../../shared/components/shared-menu/shared-menu';
import { SharedHeader } from '../../../../shared/components/shared-header/shared-header';
import { SharedHeaderConfig } from '../../../../shared/components/shared-header/shared-header-config';
import { SharedMenuConfig } from '../../../../shared/components/shared-menu/shared-menu.config';

@Component({
  standalone: true,
  selector: 'app-owners-page',
  imports: [OwnersRegisterModal, OwnersListView, SharedSidebar, SharedMenu, SharedHeader],
  templateUrl: './owners-page.html',
})
export class OwnersPage {
  owners = signal<Owner[]>([]);

  private readonly service = inject(OwnerService);
  public readonly modalStage = signal(false);

  public readonly headerConfig: SharedHeaderConfig = {
    category: 'Management',
    title: 'Owners',
    description: 'Control panel for owner management',
  };

  public readonly menuConfig: SharedMenuConfig = {
    category: 'Management',
    title: 'Owners'
  };

  public openModal(): void {
    this.modalStage.set(true);
  }

  public closeModal(): void {
    this.modalStage.set(false);
  }

  public reload(): void {
    this.service.findAll();
  }
}

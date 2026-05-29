import { Component, inject, signal } from '@angular/core';
import { SharedSidebar } from '../../../../shared/components/shared-sidebar/shared-sidebar';
import { SharedMenu } from '../../../../shared/components/shared-menu/shared-menu';
import { SharedHeader } from '../../../../shared/components/shared-header/shared-header';
import { SharedHeaderConfig } from '../../../../shared/components/shared-header/shared-header-config';
import { SharedMenuConfig } from '../../../../shared/components/shared-menu/shared-menu.config';
import { UserService } from '../../../../core/services/users.service';
import { UsersListView } from '../../components/users-list-view/users-list-view';
import { User } from '../../../../core/models/user.model';
import { UsersRegisterModal } from '../../components/users-register-modal/users-register-modal';


@Component({
  standalone: true,
  selector: 'app-users-page',
  imports: [SharedSidebar, SharedMenu, SharedHeader, UsersListView, UsersRegisterModal],
  templateUrl: './users-page.html'
})
export class UsersPage {
  users = signal<User[]>([]);
  private readonly service = inject(UserService)
  public readonly modalStage = signal(false);

  public readonly headerConfig: SharedHeaderConfig = {
    category: 'Management',
    title: 'Users',
    description: 'Control panel for users management',
  };

  public readonly menuConfig: SharedMenuConfig = {
    category: 'Management',
    title: 'Users'
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

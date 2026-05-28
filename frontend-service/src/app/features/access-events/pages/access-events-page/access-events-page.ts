import { Component, inject, signal } from '@angular/core';
import { SharedSidebar } from '../../../../shared/components/shared-sidebar/shared-sidebar';
import { SharedMenu } from '../../../../shared/components/shared-menu/shared-menu';
import { SharedHeader } from '../../../../shared/components/shared-header/shared-header';
import { SharedHeaderConfig } from '../../../../shared/components/shared-header/shared-header-config';
import { SharedMenuConfig } from '../../../../shared/components/shared-menu/shared-menu.config';
import { AccessEventsView } from '../../components/access-events-view/access-events-view';
import { AccessEventService } from '../../../../core/services/access-event.service';

@Component({
  standalone: true,
  selector: 'app-access-events-page',
  imports: [
    SharedSidebar,
    SharedMenu,
    SharedHeader,
    AccessEventsView,
  ],
  templateUrl: './access-events-page.html',
})
export class AccessEventsPage {
  private readonly accessEventService = inject(AccessEventService);

  public readonly modalStage = signal(false);

  public readonly accessEvents =
    this.accessEventService.accessEvents;

  public readonly pagination =
    this.accessEventService.pagination;

  public readonly headerConfig: SharedHeaderConfig = {
    category: 'Operation',
    title: 'Access Events',
    description: 'View the full history of vehicle entries and exits',
  };

  public readonly menuConfig: SharedMenuConfig = {
    category: 'Operation',
    title: 'Access Events',
  };

  public ngOnInit(): void {
    this.refreshEvents();
  }

  public refreshEvents(): void {
    this.accessEventService.findAll();
  }

  public handlePageChange(
    direction: 'previous' | 'next'
  ): void {
    const pagination = this.pagination();

    if (!pagination) return;

    const currentPage = pagination.number;

    if (direction === 'previous' && !pagination.first) {
      this.accessEventService.findAll(currentPage - 1);
    }

    if (direction === 'next' && !pagination.last) {
      this.accessEventService.findAll(currentPage + 1);
    }
  }

  public openModal(): void {
    this.modalStage.set(true);
  }

  public closeModal(): void {
    this.modalStage.set(false);
  }
}
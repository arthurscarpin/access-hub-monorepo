import { Component, inject } from '@angular/core';
import { OwnerService } from '../../../../core/services/owner.service';
import { NgClass } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-owners-list-view',
  imports: [NgClass],
  templateUrl: './owners-list-view.html',
})
export class OwnersListView {
  private readonly service = inject(OwnerService);

  public readonly owners = this.service.owners;
  public readonly loading = this.service.loading;
  public readonly totalElements = this.service.totalElements;
  public readonly pagination = this.service.pagination;

  public ngOnInit(): void {
    this.service.findAll();
  }

  public changePage(action: 'next' | 'previous'): void {
    const pagination = this.pagination();

    if (!pagination) return;

    if (action === 'next' && pagination.last) return;
    if (action === 'previous' && pagination.first) return;

    const nextPage = action === 'next' ? pagination.number + 1 : pagination.number - 1;

    this.service.findAll(nextPage);
  }

  public formatDocument(document: string, type: string): string {
    const value = document.replace(/\D/g, '');

    if (type === 'CPF') {
      return value
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
    }

    if (type === 'RG') {
      return value
        .replace(/(\d{2})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d{1})$/, '$1-$2');
    }

    return value;
  }
}

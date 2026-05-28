import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Paginated } from '../models/paginated.model';
import { environment } from '../../../environments/environment';
import { AccessEvent } from '../models/access-event.model';

@Injectable({ providedIn: 'root' })
export class AccessEventService {
  private readonly apiUrl = environment.apiUrl;
  private readonly httpClient = inject(HttpClient);

  private readonly _pagination = signal<Paginated<AccessEvent> | null>(null);
  public readonly pagination = this._pagination.asReadonly();

  private readonly _accessEvents = signal<AccessEvent[]>([]);
  public readonly accessEvents = this._accessEvents.asReadonly();

  private readonly _loading = signal(false);
  public readonly loading = this._loading.asReadonly();

  private readonly _totalElements = signal(0);
  public readonly totalElements = this._totalElements.asReadonly();

  public findAll(page: number = 0, size: number = 5): void {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    this.httpClient
      .get<Paginated<AccessEvent>>(`${this.apiUrl}/access-events`, { params })
      .subscribe({
        next: (res) => {
          this._pagination.set(res);
          this._accessEvents.set(res.content);
          this._totalElements.set(res.totalElements);
          this._loading.set(false);
        },
        error: () => {
          this._accessEvents.set([]);
          this._totalElements.set(0);
          this._loading.set(false);
        },
      });
  }
}

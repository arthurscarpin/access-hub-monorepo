import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Owner } from '../models/owner.model';
import { Paginated } from '../models/paginated.model';
import { CreateOwnerRequest } from '../dto/owner.dto';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class OwnerService {
  private readonly apiUrl = environment.apiUrl;
  private readonly httpClient = inject(HttpClient);

  private readonly _pagination = signal<Paginated<Owner> | null>(null);
  public readonly pagination = this._pagination.asReadonly();

  private readonly _owners = signal<Owner[]>([]);
  public readonly owners = this._owners.asReadonly();

  private readonly _loading = signal(false);
  public readonly loading = this._loading.asReadonly();

  private readonly _totalElements = signal(0);
  public readonly totalElements = this._totalElements.asReadonly();

  public findAll(page: number = 0, size: number = 8): void {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    this.httpClient.get<Paginated<Owner>>(`${this.apiUrl}/owners`, { params }).subscribe({
      next: (res) => {
        this._pagination.set(res);
        this._owners.set(res.content);
        this._totalElements.set(res.totalElements);
        this._loading.set(false);
      },
      error: () => {
        this._owners.set([]);
        this._totalElements.set(0);
        this._loading.set(false);
      },
    });
  }

  public save(payload: CreateOwnerRequest): Observable<Owner> {
    return this.httpClient.post<Owner>(`${this.apiUrl}/owners`, payload);
  }
}

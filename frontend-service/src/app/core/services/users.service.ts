import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { User } from '../models/user.model';
import { Paginated } from '../models/paginated.model';
import { CreateUserRequest } from '../dto/user.dto';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly apiUrl = environment.apiUrl;
  private readonly httpClient = inject(HttpClient);

  private readonly _pagination = signal<Paginated<User> | null>(null);
  public readonly pagination = this._pagination.asReadonly();

  private readonly _users = signal<User[]>([]);
  public readonly users = this._users.asReadonly();

  private readonly _loading = signal(false);
  public readonly loading = this._loading.asReadonly();

  private readonly _totalElements = signal(0);
  public readonly totalElements = this._totalElements.asReadonly();

  public findAll(page: number = 0, size: number = 8): void {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    this.httpClient.get<Paginated<User>>(`${this.apiUrl}/users`, { params }).subscribe({
      next: (res) => {
        this._pagination.set(res);
        this._users.set(res.content);
        this._totalElements.set(res.totalElements);
        this._loading.set(false);
      },
      error: () => {
        this._users.set([]);
        this._totalElements.set(0);
        this._loading.set(false);
      },
    });
  }

  public save(payload: CreateUserRequest): Observable<User> {
    return this.httpClient.post<User>(`${this.apiUrl}/users`, payload);
  }
}

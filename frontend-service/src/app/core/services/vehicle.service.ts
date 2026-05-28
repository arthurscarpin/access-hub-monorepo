import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Vehicle } from '../models/vehicle.model';
import { Paginated } from '../models/paginated.model';
import { CreateVehicleRequest } from '../dto/vehicle.dto';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class VehicleService {
  private readonly apiUrl = environment.apiUrl;
  private readonly httpClient = inject(HttpClient);

  private readonly _pagination = signal<Paginated<Vehicle> | null>(null);
  public readonly pagination = this._pagination.asReadonly();

  private readonly _vehicles = signal<Vehicle[]>([]);
  public readonly vehicles = this._vehicles.asReadonly();

  private readonly _loading = signal(false);
  public readonly loading = this._loading.asReadonly();

  private readonly _totalElements = signal(0);
  public readonly totalElements = this._totalElements.asReadonly();

  public findAll(page: number = 0, size: number = 5): void {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    this.httpClient.get<Paginated<Vehicle>>(`${this.apiUrl}/vehicles`, { params }).subscribe({
      next: (res) => {
        this._pagination.set(res);
        this._vehicles.set(res.content);
        this._totalElements.set(res.totalElements);
        this._loading.set(false);
      },
      error: () => {
        this._vehicles.set([]);
        this._totalElements.set(0);
        this._loading.set(false);
      },
    });
  }

  public save(payload: CreateVehicleRequest): Observable<Vehicle> {
    return this.httpClient.post<Vehicle>(`${this.apiUrl}/vehicles`, payload);
  }

  public updateById(id: string): Observable<Vehicle> {
    return this.httpClient.patch<Vehicle>(`${this.apiUrl}/vehicles/${id}`, {});
  }

  public updateVehicleInList(updated: Vehicle): void {
    this._vehicles.update((list) => list.map((v) => (v.id === updated.id ? updated : v)));
  }
}

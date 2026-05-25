import { Injectable, computed, signal, Type, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { LucideArrowDownLeft, LucideArrowUpRight } from '@lucide/angular';
import { environment } from '@core/config/evironment';
import { AccessEventConfig } from 'src/app/ui/components/shared/interfaces/ui.interfaces';
import { AccessEventsResponse } from '@core/models/access-event.models';


@Injectable({ providedIn: 'root' })
export class AccessEventsService {
  
  private readonly apiUrl = environment.apiUrl;
  private http = inject(HttpClient);
  private _events = signal<any[]>([]);

  private capitalize(value: string): string {
    if (!value) return '';
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }
 
  public events = computed<AccessEventConfig[]>(() => {
    return this._events().map((event) => ({
      id: event.id,
      plate: event.plate,
      direction: this.capitalize(event.direction),
      status: this.capitalize(event.result),
      date: event.timestamp,
      icon: (event.direction === 'AUTHORIZED' ? LucideArrowDownLeft : LucideArrowUpRight) as Type<any>,
    }));
  });

  public load(page: number = 0, size: number = 8) {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    this.http.get<AccessEventsResponse>(`${this.apiUrl}/access-events`, { params }).subscribe({
      next: (body) => {
        if (body && body.content) {
          this._events.set(body.content);
        }
      },
      error: (err) => {
        console.error('Request error: ', err);
        this._events.set([]);
      }
    });
  }
}
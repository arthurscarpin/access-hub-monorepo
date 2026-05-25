import { Injectable, computed, signal, Type, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@core/config/evironment';
import { ScopesResponse } from '@core/models/scope.models';
import { SCOPES_MAPPER } from '@ui/pages/scopes/scopes.contants';


@Injectable({ providedIn: 'root' })
export class ScopeService {
  private readonly apiUrl = environment.apiUrl;
  private http = inject(HttpClient);
  private _scopesRaw = signal<ScopesResponse[]>([]);

  public readonly scopes = computed(() => {
    return this._scopesRaw().map((scope) => {
      const [resourceKey, actionKey] = scope.name.split(':');
      const actionConfig = SCOPES_MAPPER[actionKey] || SCOPES_MAPPER['read'];
      const formattedResource = resourceKey.replace(/_/g, ' ');
      return {
        id: scope.id,
        resource: formattedResource,
        action: actionKey || 'all',
        description: actionConfig.description,
        icon: actionConfig.icon,
      };
    });
  });

  public load() {
    this.http.get<ScopesResponse[]>(`${this.apiUrl}/scopes`).subscribe({
      next: (body) => {
        if (body && body.length) {
          this._scopesRaw.set(body);
        }
      },
      error: (err) => {
        console.error('Request error: ', err);
        this._scopesRaw.set([]);
      },
    });
  }
}
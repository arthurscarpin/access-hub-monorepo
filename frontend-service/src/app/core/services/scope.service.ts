import { inject, Injectable, signal } from "@angular/core";
import { environment } from "../../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Scope } from "../models/scope.model";

@Injectable({ providedIn: 'root' })
export class ScopeService {
  private readonly apiUrl = environment.apiUrl;
  private httpClient = inject(HttpClient);
  private _scopes = signal<Scope[]>([]);
  public readonly scopes = this._scopes.asReadonly();

  public findAll(): void {
    this.httpClient.get<Scope[]>(`${this.apiUrl}/scopes`).subscribe({
      next: (res) => {
        this._scopes.set(res);
      },
      error: () => {
        this._scopes.set([]);
      },
    });
  }
}

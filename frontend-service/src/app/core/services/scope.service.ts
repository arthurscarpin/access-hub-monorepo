import { Injectable, Inject, inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "@config/evironment";


export interface ScopeResponse {
  id: string,
  name: string;
}

@Injectable({providedIn: 'root'})
export class ScopeService {
    
    private http = inject(HttpClient);
    private apiUrl = environment.apiUrl;

    getScopes(): Observable<ScopeResponse[]> {
        return this.http.get<ScopeResponse[]>(`${this.apiUrl}/scopes`);
    }
}
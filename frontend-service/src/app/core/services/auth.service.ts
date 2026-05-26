import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { LoginInfoResponse, LoginRequest, LoginResponse } from '../dto/login.dto';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = environment.apiUrl;
  private readonly httpClient = inject(HttpClient);
  private readonly router = inject(Router);

  private saveToken(token: string, rememberMe: boolean): void {
    this._removeToken();
    if (rememberMe) {
      localStorage.setItem('accessToken', token);
    } else {
      sessionStorage.setItem('accessToken', token);
    }
  }

  private _removeToken(): void {
    localStorage.removeItem('accessToken');
    sessionStorage.removeItem('accessToken');
  }

  public login(payload: LoginRequest): Observable<LoginResponse> {
    return this.httpClient.post<LoginResponse>(`${this.apiUrl}/login`, payload).pipe(
      tap((response) => {
        if (response?.accessToken) {
          this.saveToken(response.accessToken, payload.rememberMe);
        }
      }),
    );
  }

  public getToken(): string | null {
    return localStorage.getItem('accessToken') || sessionStorage.getItem('accessToken');
  }

  public logout(): void {
    this._removeToken();
    this.router.navigate(['/login']);
  }

  public checkLoginStage(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const decoded: any = jwtDecode(token);
      if (!decoded.exp) return false;
      const expirationDate = decoded.exp * 1000;
      return Date.now() < expirationDate;
    } catch {
      return false;
    }
  }

  public getLoginInfo(): LoginInfoResponse {
    const token = this.getToken();
    if (!token) return { email: '', username: '' };
    try {
      const decodedToken: any = jwtDecode(token);
      const email = decodedToken.email || '';
      const username = email ? email.split('@')[0] : '';
      return { email, username };
    } catch {
      return { email: '', username: '' };
    }
  }
}

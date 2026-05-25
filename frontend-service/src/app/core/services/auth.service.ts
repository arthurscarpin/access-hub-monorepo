import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { environment } from '@core/config/evironment';
import { AuthResponse } from '@core/models/auth.models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private readonly apiUrl = environment.apiUrl;
  private readonly TOKEN_KEY = 'accessToken';

  private saveToken(token: string, rememberMe: boolean): void {
    const storage = rememberMe ? localStorage : sessionStorage;
    storage.setItem(this.TOKEN_KEY, token);
  }

  login(email: string, password: string, rememberMe: boolean) {
    return this.http
      .post<AuthResponse>(`${this.apiUrl}/login`, { email, password })
      .pipe(
        tap((response) => {
          if (response?.accessToken) {
            this.saveToken(response.accessToken, rememberMe);
          }
        })
      );
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY) || sessionStorage.getItem(this.TOKEN_KEY);
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    sessionStorage.removeItem(this.TOKEN_KEY);
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getUsernameAndEmail(): { email: string; username: string } {
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
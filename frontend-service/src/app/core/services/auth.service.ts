import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@config/evironment';
import { LoginResponse } from '@core/models/auth.models';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;
  private readonly TOKEN_KEY = 'accessToken';

  login(email: string, password: string) {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, {
      email,
      password,
    });
  }

  saveToken(token: string, rememberMe: boolean = true): void {
    const storage = rememberMe ? localStorage : sessionStorage;
    storage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return (
      localStorage.getItem(this.TOKEN_KEY) ||
      sessionStorage.getItem(this.TOKEN_KEY)
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    sessionStorage.removeItem(this.TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
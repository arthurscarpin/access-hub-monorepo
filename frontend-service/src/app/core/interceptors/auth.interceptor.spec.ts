import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { AuthService } from '../services/auth.service';
import { authInterceptor } from './auth.interceptor';

describe('authInterceptor', () => {
  let httpClient: HttpClient;
  let http: HttpTestingController;
  let authService: {
    getToken: ReturnType<typeof vi.fn>;
    logout: ReturnType<typeof vi.fn>;
  };

  beforeEach(() => {
    authService = {
      getToken: vi.fn().mockReturnValue('token-123'),
      logout: vi.fn(),
    };

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
        { provide: AuthService, useValue: authService },
      ],
    });

    httpClient = TestBed.inject(HttpClient);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('adds authorization header to protected requests', () => {
    httpClient.get(`${environment.apiUrl}/vehicles`).subscribe();

    const req = http.expectOne(`${environment.apiUrl}/vehicles`);
    expect(req.request.headers.get('Authorization')).toBe('Bearer token-123');
    req.flush([]);
  });

  it('does not add authorization header to login, scopes and user creation requests', () => {
    httpClient.post(`${environment.apiUrl}/login`, {}).subscribe();
    httpClient.get(`${environment.apiUrl}/scopes`).subscribe();
    httpClient.post(`${environment.apiUrl}/users`, {}).subscribe();

    for (const request of http.match(() => true)) {
      expect(request.request.headers.has('Authorization')).toBe(false);
      request.flush({});
    }
  });

  it('logs out when a protected request returns unauthorized', () => {
    httpClient.get(`${environment.apiUrl}/vehicles`).subscribe({
      error: () => undefined,
    });

    http.expectOne(`${environment.apiUrl}/vehicles`).flush(
      { message: 'unauthorized' },
      { status: 401, statusText: 'Unauthorized' },
    );

    expect(authService.logout).toHaveBeenCalled();
  });
});

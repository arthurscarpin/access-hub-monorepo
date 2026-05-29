import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';

import { environment } from '../../../environments/environment';
import { AuthService } from './auth.service';

function tokenWith(payload: Record<string, unknown>): string {
  const encode = (value: unknown) =>
    globalThis
      .btoa(JSON.stringify(value))
      .replace(/\+/g, '-')
      .replace(/\//g, '_')
      .replace(/=+$/, '');

  return `${encode({ alg: 'none', typ: 'JWT' })}.${encode(payload)}.`;
}

describe('AuthService', () => {
  let service: AuthService;
  let http: HttpTestingController;
  let router: { navigate: ReturnType<typeof vi.fn> };

  beforeEach(() => {
    router = { navigate: vi.fn() };

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: Router, useValue: router },
      ],
    });

    service = TestBed.inject(AuthService);
    http = TestBed.inject(HttpTestingController);
    localStorage.clear();
    sessionStorage.clear();
  });

  afterEach(() => {
    http.verify();
    localStorage.clear();
    sessionStorage.clear();
  });

  it('saves the access token in localStorage when rememberMe is enabled', () => {
    const payload = { email: 'admin@accesshub.test', password: 'secret123', rememberMe: true };

    service.login(payload).subscribe((response) => {
      expect(response.accessToken).toBe('token-123');
    });

    const req = http.expectOne(`${environment.apiUrl}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush({ accessToken: 'token-123' });

    expect(localStorage.getItem('accessToken')).toBe('token-123');
    expect(sessionStorage.getItem('accessToken')).toBeNull();
  });

  it('saves the access token in sessionStorage when rememberMe is disabled', () => {
    service
      .login({ email: 'admin@accesshub.test', password: 'secret123', rememberMe: false })
      .subscribe();

    http.expectOne(`${environment.apiUrl}/login`).flush({ accessToken: 'session-token' });

    expect(sessionStorage.getItem('accessToken')).toBe('session-token');
    expect(localStorage.getItem('accessToken')).toBeNull();
  });

  it('clears tokens and redirects on logout', () => {
    localStorage.setItem('accessToken', 'stored');
    sessionStorage.setItem('accessToken', 'session');

    service.logout();

    expect(localStorage.getItem('accessToken')).toBeNull();
    expect(sessionStorage.getItem('accessToken')).toBeNull();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('validates token expiration', () => {
    localStorage.setItem('accessToken', tokenWith({ exp: Math.floor(Date.now() / 1000) + 60 }));

    expect(service.checkLoginStage()).toBe(true);

    localStorage.setItem('accessToken', tokenWith({ exp: Math.floor(Date.now() / 1000) - 60 }));

    expect(service.checkLoginStage()).toBe(false);
  });

  it('extracts login info from the token email', () => {
    localStorage.setItem('accessToken', tokenWith({ email: 'arthur@example.com' }));

    expect(service.getLoginInfo()).toEqual({
      email: 'arthur@example.com',
      username: 'arthur',
    });
  });
});

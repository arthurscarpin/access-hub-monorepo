import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { AuthService } from '../../../../core/services/auth.service';
import { LoginCard } from './login-card';

describe('LoginCard', () => {
  let component: LoginCard;
  let fixture: ComponentFixture<LoginCard>;
  let authService: { login: ReturnType<typeof vi.fn> };
  let router: { navigateByUrl: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    authService = { login: vi.fn() };
    router = { navigateByUrl: vi.fn() };

    await TestBed.configureTestingModule({
      imports: [LoginCard],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginCard);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should validate the form before submitting', () => {
    component.submit();

    expect(component.form.controls.email.touched).toBe(true);
    expect(authService.login).not.toHaveBeenCalled();
  });

  it('should login and navigate to dashboard with valid credentials', () => {
    authService.login.mockReturnValue(of({ accessToken: 'token-123' }));
    component.form.setValue({
      email: 'admin@accesshub.test',
      password: 'secret123',
      rememberMe: true,
    });

    component.submit();

    expect(authService.login).toHaveBeenCalledWith(component.form.getRawValue());
    expect(router.navigateByUrl).toHaveBeenCalledWith('/dashboard');
  });

  it('should show backend errors and toggle password visibility', () => {
    authService.login.mockReturnValue(
      throwError(() => ({ error: { message: 'Invalid credentials' } })),
    );
    component.form.setValue({
      email: 'admin@accesshub.test',
      password: 'secret123',
      rememberMe: false,
    });

    component.togglePassword();
    component.submit();

    expect(component.showPassword()).toBe(true);
    expect(component.errorMessage()).toBe('Invalid credentials');
  });
});

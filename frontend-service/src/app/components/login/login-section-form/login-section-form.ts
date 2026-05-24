import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import {
  LucideMail,
  LucideLock,
  LucideEye,
  LucideEyeOff,
  LucideArrowRight
} from '@lucide/angular';

import { AuthService } from '@core/services/auth.service';
import { Button } from '@components/shared/button/button';

@Component({
  standalone: true,
  selector: 'app-login-section-form',
  imports: [
    ReactiveFormsModule,
    LucideMail,
    LucideLock,
    LucideEye,
    LucideEyeOff,
    LucideArrowRight,
    Button,
  ],
  templateUrl: './login-section-form.html',
})
export class LoginSectionForm {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  errorMessage = signal('');
  isLoading = signal(false);
  showPassword = signal(false);

  isForgotOpen = signal(false);
  forgotEmail = signal('');
  forgotEmailError = signal('');

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    rememberMe: [false],
  });

  constructor() {
    this.form.valueChanges.subscribe(() => {
      this.errorMessage.set('');
    });
  }

  onSubmit() {
    if (this.form.invalid || this.isLoading()) return;

    this.isLoading.set(true);
    this.errorMessage.set('');

    const { email, password, rememberMe } = this.form.getRawValue();

    this.authService.login(email!, password!).subscribe({
      next: (response) => {
        this.authService.saveToken(response.accessToken, rememberMe ?? false);
        this.isLoading.set(false);
        this.router.navigateByUrl('/dashboard');
      },
      error: (error) => {
        this.isLoading.set(false);
        this.errorMessage.set(error?.error?.message || 'Login failed');
      },
    });
  }

  togglePassword() {
    this.showPassword.update(v => !v);
  }

  private resetForgotPasswordState() {
    this.forgotEmail.set('');
    this.forgotEmailError.set('');
  }

  openForgotPassword() {
    this.resetForgotPasswordState();
    this.isForgotOpen.set(true);
  }

  closeForgotPassword() {
    this.isForgotOpen.set(false);
    this.resetForgotPasswordState();
  }

  onForgotEmailChange(value: string) {
    this.forgotEmail.set(value);
    this.forgotEmailError.set('');
  }

  sendRecoveryEmail() {
    const email = this.forgotEmail().trim();

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!email) {
      this.forgotEmailError.set('Email is required');
      return;
    }

    if (!emailRegex.test(email)) {
      this.forgotEmailError.set('Please enter a valid email');
      return;
    }

    this.forgotEmailError.set('');

    console.log('Sending recovery email to:', email);

    // this.authService.sendRecoveryEmail(email).subscribe(...)

    this.closeForgotPassword();
  }
}
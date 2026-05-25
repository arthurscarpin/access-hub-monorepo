import { Component, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LucideMail, LucideLock, LucideEye, LucideEyeOff, LucideArrowRight } from '@lucide/angular';
import { AuthService } from '@core/services/auth.service';

@Component({
  standalone: true,
  selector: 'app-login-section-form',
  imports: [
    ReactiveFormsModule,
    LucideMail,
    LucideLock,
    LucideEye,
    LucideEyeOff,
    LucideArrowRight
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

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    rememberMe: [false],
  });

  forgotEmailControl = this.fb.control('', [Validators.required, Validators.email]);

  constructor() {
    this.form.valueChanges
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.errorMessage.set(''));
  }

  onSubmit(): void {
    if (this.form.invalid || this.isLoading()) return;
    this.isLoading.set(true);
    this.errorMessage.set('');
    const { email, password, rememberMe } = this.form.getRawValue();
    this.authService.login(email!, password!, !!rememberMe).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.router.navigateByUrl('/dashboard');
      },
      error: (error) => {
        this.isLoading.set(false);
        this.errorMessage.set(error?.error?.message || 'Login failed');
      },
    });
  }

  togglePassword(): void {
    this.showPassword.update(v => !v);
  }

  openForgotPassword(): void {
    this.forgotEmailControl.reset();
    this.isForgotOpen.set(true);
  }

  closeForgotPassword(): void {
    this.isForgotOpen.set(false);
  }

  sendRecoveryEmail(): void {
    if (this.forgotEmailControl.invalid) {
      this.forgotEmailControl.markAsTouched();
      return;
    }
    const email = this.forgotEmailControl.value?.trim();
    console.log('Sending recovery email to:', email);
    // this.authService.sendRecoveryEmail(email!).subscribe(...)
    this.closeForgotPassword();
  }
}
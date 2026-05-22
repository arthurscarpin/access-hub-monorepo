import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LucideMail, LucideLock, LucideEye, LucideEyeOff, LucideArrowRight } from '@lucide/angular';
import { AuthService } from '@core/services/auth.service';

@Component({
  standalone: true,
  selector: 'app-login-section-form',
  imports: [ReactiveFormsModule, LucideMail, LucideLock, LucideEye, LucideEyeOff, LucideArrowRight],
  templateUrl: './login-section-form.html',
})
export class LoginSectionForm {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  errorMessage = signal('');
  isLoading = signal(false);
  showPassword = signal(false);

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    rememberMe: [false]
  })

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
        this.errorMessage.set(error?.error?.message);
      }
    })
  }

  togglePassword() {
    this.showPassword.update(v => !v);
  }
}

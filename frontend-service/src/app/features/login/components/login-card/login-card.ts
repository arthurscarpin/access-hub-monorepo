import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LucideMail, LucideLock, LucideEye, LucideEyeOff, LucideArrowRight } from '@lucide/angular';
import { LoginRequest } from '../../../../core/dto/login.dto';
import { LoginForgotPasswordModal } from '../login-forgot-password-modal/login-forgot-password-modal';
import { AuthService } from '../../../../core/services/auth.service';


@Component({
  standalone: true,
  selector: 'app-login-card',
  imports: [ReactiveFormsModule, LucideMail, LucideLock, LucideEye, LucideEyeOff, LucideArrowRight, LoginForgotPasswordModal],
  templateUrl: './login-card.html',
})
export class LoginCard {
  private readonly formBuilder = inject(FormBuilder);
  private readonly service = inject(AuthService);
  private readonly router = inject(Router);

  public readonly showPassword = signal(false);
  public readonly errorMessage = signal('');
  public readonly modalStage = signal(false);

  public openModal(): void {
    this.modalStage.set(true);
  }

  public closeModal(): void {
    this.modalStage.set(false);
  }

  public readonly form = this.formBuilder.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    rememberMe: [false],
  });
  
  public togglePassword(): void {
    this.showPassword.update((value) => !value);
  }

  public submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const payload: LoginRequest = this.form.getRawValue();
    this.service.login(payload).subscribe({
      next: () => this.router.navigateByUrl('/dashboard'),
      error: (error) => this.errorMessage.set(error?.error?.message)
    });
  }
}

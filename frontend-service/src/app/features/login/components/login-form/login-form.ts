import { Component, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LucideAngularModule, Mail, Lock, ArrowRight, Eye } from 'lucide-angular';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: '[app-login-form]',
  standalone: true,
  imports: [LucideAngularModule, ReactiveFormsModule],
  templateUrl: './login-form.html',
})
export class LoginForm {
  readonly Mail = Mail;
  readonly Lock = Lock;
  readonly ArrowRight = ArrowRight;
  readonly Eye = Eye;

  form: FormGroup;

  errorMessage = '';
  isLoading = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {
    this.form = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rememberMe: [false],
    });

    this.form.valueChanges.subscribe(() => {
      this.errorMessage = '';
      this.cdr.detectChanges();
    });
  }

  onSubmit() {
    if (this.form.invalid || this.isLoading) return;

    this.isLoading = true;
    this.errorMessage = '';

    const { email, password, rememberMe } = this.form.value;

    this.authService.login(email!, password!).subscribe({
      next: (res) => {
        this.authService.saveToken(res.accessToken, rememberMe);
        this.isLoading = false;
        this.router.navigateByUrl('/dashboard');
      },

      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err?.error?.message || err?.message || 'Login failed. Try again.';
        this.cdr.detectChanges();
      },
    });
  }
}

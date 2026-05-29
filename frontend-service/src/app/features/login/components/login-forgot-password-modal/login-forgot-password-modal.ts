import { Component, output, signal, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-login-forgot-password-modal',
  imports: [ReactiveFormsModule],
  templateUrl: './login-forgot-password-modal.html',
})
export class LoginForgotPasswordModal {
  private formBuilder = inject(FormBuilder);

  public readonly close = output<void>();
  public readonly errorMessage = signal('');

  public readonly form = this.formBuilder.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
  });

  public submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.errorMessage.set('Please enter a valid email address.');
      return;
    }
    const {email} = this.form.getRawValue();
    console.log(email);
  }
}

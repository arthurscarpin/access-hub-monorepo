import { Component, inject, output, signal } from '@angular/core';
import { NgClass } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { ScopeService } from '../../../../core/services/scope.service';
import { CreateUserRequest } from '../../../../core/dto/users.dto';
import { UserService } from '../../../../core/services/users.service';

@Component({
  standalone: true,
  selector: 'app-users-register-modal',
  imports: [NgClass, ReactiveFormsModule],
  templateUrl: './users-register-modal.html',
})
export class UsersRegisterModal {
  private readonly scopeService = inject(ScopeService);
  private readonly userService = inject(UserService);
  private readonly formBuilder = inject(FormBuilder);

  public readonly close = output<void>();
  public readonly saved = output<void>();

  public readonly scopes = this.scopeService.scopes;

  public readonly error = signal<string | null>(null);

  public readonly form = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    scopes: this.formBuilder.nonNullable.control<string[]>([]),
  });

  public ngOnInit(): void {
    this.scopeService.findAll();
  }

  public toggleScope(scopeId: string, checked: boolean): void {
    const current = this.form.controls.scopes.value;

    if (checked) {
      if (!current.includes(scopeId)) {
        this.form.controls.scopes.setValue([...current, scopeId]);
      }
      return;
    }

    this.form.controls.scopes.setValue(current.filter((id) => id !== scopeId));
  }

  public submit(): void {
    this.error.set(null);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.error.set('Please fill all required fields correctly.');
      return;
    }

    const payload: CreateUserRequest = this.form.getRawValue();
    this.userService.save(payload).subscribe({
      next: () => {
        this.form.reset();
        this.saved.emit();
        this.close.emit();
      },
      error: () => {
        this.error.set('Failed to create user');
      },
    });

    this.saved.emit();
  }

  public getScopeVariant(scope: string): 'all' | 'write' | 'read' {
    return scope.split(':')[1] as 'all' | 'write' | 'read';
  }
}

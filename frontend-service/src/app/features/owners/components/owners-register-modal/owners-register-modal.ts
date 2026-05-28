import { Component, output, inject, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { OwnerService } from '../../../../core/services/owner.service';
import { CreateOwnerRequest } from '../../../../core/dto/owner.dto';

@Component({
  standalone: true,
  selector: 'app-owners-register-modal',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './owners-register-modal.html',
})
export class OwnersRegisterModal {
  private readonly formBuilder = inject(FormBuilder);
  private readonly service = inject(OwnerService);
  public readonly saved = output<void>();
  public readonly close = output<void>();
  private readonly documentRules = { CPF: 11, RG: 9 } as const;
  public readonly error = signal<string | null>(null);

  public readonly form = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    documentType: ['', Validators.required],
    document: ['', Validators.required],
  });

  ngOnInit(): void {
    this.form.get('documentType')!.valueChanges.subscribe((type) => {
      const control = this.form.get('document')!;
      const maxLength = this.documentRules[type as keyof typeof this.documentRules];
      if (!maxLength) return;
      control.setValue('');
      control.setValidators([
        Validators.required,
        Validators.maxLength(maxLength),
        Validators.minLength(maxLength),
      ]);
      control.updateValueAndValidity();
    });
  }

  public submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: CreateOwnerRequest = this.form.getRawValue();
    this.service.save(payload).subscribe({
      next: () => {
        this.form.reset();
        this.saved.emit();
        this.close.emit();
      },
      error: (err) => {
        const statusError = err?.status;
        let messageError = err?.error?.message;

        if (statusError === 500) {
          console.log(messageError);
          messageError = 'Server error! Please contact ADMIN';
        }

        this.error.set(messageError || 'Failed to create owner');
      },
    });
  }
}

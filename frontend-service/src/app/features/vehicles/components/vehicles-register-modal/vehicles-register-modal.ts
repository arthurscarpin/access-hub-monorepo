import { Component, inject, output } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { VehicleService } from '../../../../core/services/vehicle.service';
import { CreateVehicleRequest } from '../../../../core/dto/vehicle.dto';

@Component({
  standalone: true,
  selector: 'app-vehicles-register-modal',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './vehicles-register-modal.html',
})
export class VehiclesRegisterModal {
  private readonly formBuilder = inject(FormBuilder);
  private readonly service = inject(VehicleService);

  public readonly saved = output<void>();
  public readonly close = output<void>();

  public readonly form = this.formBuilder.nonNullable.group({
    plate: [
      '',
      [
        Validators.required,
        Validators.pattern(/^[A-Z]{3}[0-9]{1}[A-Z]{1}[0-9]{2}$|^[A-Z]{3}[0-9]{4}$/),
      ],
    ],
    model: ['', [Validators.required, Validators.minLength(2)]],
    ownerId: ['', [Validators.required]],
  });

  public submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: CreateVehicleRequest = this.form.getRawValue();

    this.service.save(payload).subscribe({
      next: (response) => {
        this.form.reset();
        this.saved.emit();
        this.close.emit();
      },
      error: (error) => {
        console.error('Failed to create vehicle:', error);
      },
    });
  }
}

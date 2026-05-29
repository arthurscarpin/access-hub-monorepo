import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';

import { VehicleService } from '../../../../core/services/vehicle.service';
import { VehiclesRegisterModal } from './vehicles-register-modal';

describe('VehiclesRegisterModal', () => {
  let component: VehiclesRegisterModal;
  let fixture: ComponentFixture<VehiclesRegisterModal>;
  let service: { save: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    service = { save: vi.fn() };

    await TestBed.configureTestingModule({
      imports: [VehiclesRegisterModal],
      providers: [{ provide: VehicleService, useValue: service }],
    }).compileComponents();

    fixture = TestBed.createComponent(VehiclesRegisterModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should validate plate, model and owner before saving', () => {
    component.form.setValue({ plate: 'INVALID', model: 'A', ownerId: '' });

    component.submit();

    expect(service.save).not.toHaveBeenCalled();
    expect(component.form.controls.plate.invalid).toBe(true);
    expect(component.form.controls.model.invalid).toBe(true);
    expect(component.form.controls.ownerId.invalid).toBe(true);
  });

  it('should save and emit close events on success', () => {
    const savedSpy = vi.fn();
    const closeSpy = vi.fn();
    component.saved.subscribe(savedSpy);
    component.close.subscribe(closeSpy);
    service.save.mockReturnValue(of({ id: 'vehicle-1' }));
    component.form.setValue({
      plate: 'ABC1D23',
      model: 'Civic',
      ownerId: 'owner-1',
    });

    component.submit();

    expect(service.save).toHaveBeenCalledWith({
      plate: 'ABC1D23',
      model: 'Civic',
      ownerId: 'owner-1',
    });
    expect(savedSpy).toHaveBeenCalledOnce();
    expect(closeSpy).toHaveBeenCalledOnce();
  });

  it('should map owner validation errors', () => {
    service.save.mockReturnValue(
      throwError(() => ({ status: 400, error: { message: 'owner not found' } })),
    );
    component.form.setValue({
      plate: 'ABC1D23',
      model: 'Civic',
      ownerId: 'missing',
    });

    component.submit();

    expect(component.error()).toBe('Owner is invalid');
  });
});

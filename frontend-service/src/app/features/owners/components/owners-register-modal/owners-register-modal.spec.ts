import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';

import { OwnerService } from '../../../../core/services/owner.service';
import { OwnersRegisterModal } from './owners-register-modal';

describe('OwnersRegisterModal', () => {
  let component: OwnersRegisterModal;
  let fixture: ComponentFixture<OwnersRegisterModal>;
  let service: { save: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    service = { save: vi.fn() };

    await TestBed.configureTestingModule({
      imports: [OwnersRegisterModal],
      providers: [{ provide: OwnerService, useValue: service }],
    }).compileComponents();

    fixture = TestBed.createComponent(OwnersRegisterModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should update document validators when document type changes', () => {
    component.form.controls.documentType.setValue('CPF');
    component.form.controls.document.setValue('123');

    expect(component.form.controls.document.hasError('minlength')).toBe(true);

    component.form.controls.document.setValue('12345678901');

    expect(component.form.controls.document.valid).toBe(true);
  });

  it('should not save invalid forms', () => {
    component.submit();

    expect(component.form.controls.name.touched).toBe(true);
    expect(service.save).not.toHaveBeenCalled();
  });

  it('should save, reset and emit close events on success', () => {
    const savedSpy = vi.fn();
    const closeSpy = vi.fn();
    component.saved.subscribe(savedSpy);
    component.close.subscribe(closeSpy);
    service.save.mockReturnValue(of({ id: 'owner-1' }));
    component.form.setValue({
      name: 'Maria Owner',
      email: 'maria@example.com',
      documentType: 'CPF',
      document: '12345678901',
    });

    component.submit();

    expect(service.save).toHaveBeenCalledWith({
      name: 'Maria Owner',
      email: 'maria@example.com',
      documentType: 'CPF',
      document: '12345678901',
    });
    expect(savedSpy).toHaveBeenCalledOnce();
    expect(closeSpy).toHaveBeenCalledOnce();
  });

  it('should show a friendly server error', () => {
    service.save.mockReturnValue(
      throwError(() => ({ status: 500, error: { message: 'database down' } })),
    );
    component.form.setValue({
      name: 'Maria Owner',
      email: 'maria@example.com',
      documentType: 'CPF',
      document: '12345678901',
    });

    component.submit();

    expect(component.error()).toBe('Server error! Please contact ADMIN');
  });
});

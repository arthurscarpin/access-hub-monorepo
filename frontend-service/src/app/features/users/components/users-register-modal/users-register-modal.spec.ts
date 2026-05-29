import { ComponentFixture, TestBed } from '@angular/core/testing';
import { signal } from '@angular/core';
import { of, throwError } from 'rxjs';

import { ScopeService } from '../../../../core/services/scope.service';
import { UserService } from '../../../../core/services/users.service';
import { UsersRegisterModal } from './users-register-modal';

describe('UsersRegisterModal', () => {
  let component: UsersRegisterModal;
  let fixture: ComponentFixture<UsersRegisterModal>;
  let userService: { save: ReturnType<typeof vi.fn> };
  let scopeService: { scopes: ReturnType<typeof signal>; findAll: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    userService = { save: vi.fn() };
    scopeService = {
      scopes: signal([{ id: 'scope-1', name: 'vehicles:read' }]),
      findAll: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [UsersRegisterModal],
      providers: [
        { provide: UserService, useValue: userService },
        { provide: ScopeService, useValue: scopeService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UsersRegisterModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load scopes on init and toggle selected scopes', () => {
    expect(scopeService.findAll).toHaveBeenCalledWith();

    component.toggleScope('scope-1', true);
    component.toggleScope('scope-2', true);
    component.toggleScope('scope-1', false);

    expect(component.form.controls.scopes.value).toEqual(['scope-2']);
  });

  it('should show validation errors and avoid saving invalid forms', () => {
    component.submit();

    expect(component.error()).toBe('Please fill all required fields correctly.');
    expect(userService.save).not.toHaveBeenCalled();
  });

  it('should save once and emit events only after success', () => {
    const savedSpy = vi.fn();
    const closeSpy = vi.fn();
    component.saved.subscribe(savedSpy);
    component.close.subscribe(closeSpy);
    userService.save.mockReturnValue(of({ id: 'user-1' }));
    component.form.setValue({
      name: 'Admin',
      email: 'admin@accesshub.test',
      password: 'secret123',
      scopes: ['scope-1'],
    });

    component.submit();

    expect(userService.save).toHaveBeenCalledOnce();
    expect(savedSpy).toHaveBeenCalledOnce();
    expect(closeSpy).toHaveBeenCalledOnce();
  });

  it('should map server errors to user friendly messages', () => {
    userService.save.mockReturnValue(
      throwError(() => ({ status: 500, error: { message: 'database down' } })),
    );
    component.form.setValue({
      name: 'Admin',
      email: 'admin@accesshub.test',
      password: 'secret123',
      scopes: ['scope-1'],
    });

    component.submit();

    expect(component.error()).toBe('Server error! Please contact ADMIN');
  });
});

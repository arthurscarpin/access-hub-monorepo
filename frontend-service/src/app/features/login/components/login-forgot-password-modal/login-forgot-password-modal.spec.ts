import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginForgotPasswordModal } from './login-forgot-password-modal';

describe('LoginForgotPasswordModal', () => {
  let component: LoginForgotPasswordModal;
  let fixture: ComponentFixture<LoginForgotPasswordModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginForgotPasswordModal],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginForgotPasswordModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should mark invalid email and show a validation message', () => {
    component.submit();

    expect(component.form.controls.email.touched).toBe(true);
    expect(component.errorMessage()).toBe('Please enter a valid email address.');
  });

  it('should accept a valid email without setting an error', () => {
    component.form.controls.email.setValue('owner@accesshub.test');

    component.submit();

    expect(component.errorMessage()).toBe('');
  });
});

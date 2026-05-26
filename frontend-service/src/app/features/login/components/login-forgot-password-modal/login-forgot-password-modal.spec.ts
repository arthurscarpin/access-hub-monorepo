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
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

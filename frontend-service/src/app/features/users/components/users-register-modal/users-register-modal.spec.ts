import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsersRegisterModal } from './users-register-modal';

describe('UsersRegisterModal', () => {
  let component: UsersRegisterModal;
  let fixture: ComponentFixture<UsersRegisterModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UsersRegisterModal],
    }).compileComponents();

    fixture = TestBed.createComponent(UsersRegisterModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

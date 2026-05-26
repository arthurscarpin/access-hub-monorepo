import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OwnersRegisterModal } from './owners-register-modal';

describe('OwnersRegisterModal', () => {
  let component: OwnersRegisterModal;
  let fixture: ComponentFixture<OwnersRegisterModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OwnersRegisterModal],
    }).compileComponents();

    fixture = TestBed.createComponent(OwnersRegisterModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

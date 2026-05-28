import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehiclesRegisterModal } from './vehicles-register-modal';

describe('VehiclesRegisterModal', () => {
  let component: VehiclesRegisterModal;
  let fixture: ComponentFixture<VehiclesRegisterModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VehiclesRegisterModal],
    }).compileComponents();

    fixture = TestBed.createComponent(VehiclesRegisterModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

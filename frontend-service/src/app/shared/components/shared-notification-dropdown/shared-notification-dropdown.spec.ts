import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedNotificationDropdown } from './shared-notification-dropdown';

describe('SharedNotificationDropdown', () => {
  let component: SharedNotificationDropdown;
  let fixture: ComponentFixture<SharedNotificationDropdown>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedNotificationDropdown],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedNotificationDropdown);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

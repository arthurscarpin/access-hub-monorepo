import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccessEvents } from './access-events';

describe('AccessEvents', () => {
  let component: AccessEvents;
  let fixture: ComponentFixture<AccessEvents>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccessEvents],
    }).compileComponents();

    fixture = TestBed.createComponent(AccessEvents);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

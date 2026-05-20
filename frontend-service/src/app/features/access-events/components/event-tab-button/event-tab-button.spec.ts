import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventTabButton } from './event-tab-button';

describe('EventTabButton', () => {
  let component: EventTabButton;
  let fixture: ComponentFixture<EventTabButton>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventTabButton],
    }).compileComponents();

    fixture = TestBed.createComponent(EventTabButton);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventStatusBadge } from './event-status-badge';

describe('EventStatusBadge', () => {
  let component: EventStatusBadge;
  let fixture: ComponentFixture<EventStatusBadge>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventStatusBadge],
    }).compileComponents();

    fixture = TestBed.createComponent(EventStatusBadge);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

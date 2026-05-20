import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventDirectionBadge } from './event-direction-badge';

describe('DirectionBadge', () => {
  let component: EventDirectionBadge;
  let fixture: ComponentFixture<EventDirectionBadge>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventDirectionBadge],
    }).compileComponents();

    fixture = TestBed.createComponent(EventDirectionBadge);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

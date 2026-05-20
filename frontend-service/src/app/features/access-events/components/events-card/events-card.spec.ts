import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventsCard } from './events-card';

describe('EventsCard', () => {
  let component: EventsCard;
  let fixture: ComponentFixture<EventsCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventsCard],
    }).compileComponents();

    fixture = TestBed.createComponent(EventsCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

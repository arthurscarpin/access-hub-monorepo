import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventPageActions } from './event-page-actions';

describe('EventPageActions', () => {
  let component: EventPageActions;
  let fixture: ComponentFixture<EventPageActions>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventPageActions],
    }).compileComponents();

    fixture = TestBed.createComponent(EventPageActions);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

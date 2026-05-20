import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventPageTitle } from './event-page-title';

describe('EventPageTitle', () => {
  let component: EventPageTitle;
  let fixture: ComponentFixture<EventPageTitle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventPageTitle],
    }).compileComponents();

    fixture = TestBed.createComponent(EventPageTitle);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

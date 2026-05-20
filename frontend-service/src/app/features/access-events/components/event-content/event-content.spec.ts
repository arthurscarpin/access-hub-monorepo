import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventContent } from './event-content';

describe('EventContent', () => {
  let component: EventContent;
  let fixture: ComponentFixture<EventContent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventContent],
    }).compileComponents();

    fixture = TestBed.createComponent(EventContent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

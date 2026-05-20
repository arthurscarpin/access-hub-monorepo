import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventPaginationButton } from './event-pagination-button';

describe('EventPaginationButton', () => {
  let component: EventPaginationButton;
  let fixture: ComponentFixture<EventPaginationButton>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventPaginationButton],
    }).compileComponents();

    fixture = TestBed.createComponent(EventPaginationButton);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

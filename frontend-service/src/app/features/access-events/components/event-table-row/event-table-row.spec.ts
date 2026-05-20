import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventTableRow } from './event-table-row';

describe('EventTableRow', () => {
  let component: EventTableRow;
  let fixture: ComponentFixture<EventTableRow>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventTableRow],
    }).compileComponents();

    fixture = TestBed.createComponent(EventTableRow);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventTTable } from './event-table';

describe('EventTTable', () => {
  let component: EventTTable;
  let fixture: ComponentFixture<EventTTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventTTable],
    }).compileComponents();

    fixture = TestBed.createComponent(EventTTable);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

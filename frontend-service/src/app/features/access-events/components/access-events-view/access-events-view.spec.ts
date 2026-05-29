import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccessEvent } from '../../../../core/models/access-event.model';
import { paginatedFixture } from '../../../../testing/test-fixtures';
import { AccessEventsView } from './access-events-view';

describe('AccessEventsView', () => {
  let component: AccessEventsView;
  let fixture: ComponentFixture<AccessEventsView>;
  const events: AccessEvent[] = [
    {
      id: 'event-1',
      plate: 'ABC1D23',
      direction: 'IN',
      result: 'AUTHORIZED',
      timestamp: '2026-05-29T10:00:00Z',
    },
    {
      id: 'event-2',
      plate: 'XYZ9A87',
      direction: 'OUT',
      result: 'DENIED',
      timestamp: '2026-05-29T11:00:00Z',
    },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccessEventsView],
    }).compileComponents();

    fixture = TestBed.createComponent(AccessEventsView);
    component = fixture.componentInstance;
    component.events = events;
    component.pagination = paginatedFixture(events, { first: false, last: false });
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should filter events by result', () => {
    component.selectFilter('Denied');

    expect(component.filteredEvents().map((event) => event.plate)).toEqual(['XYZ9A87']);

    component.selectFilter('Authorized');

    expect(component.filteredEvents().map((event) => event.plate)).toEqual(['ABC1D23']);
  });

  it('should render the event table and emit page changes', () => {
    const emitted: string[] = [];
    component.pageChange.subscribe((direction) => emitted.push(direction));

    component.changePage('next');
    fixture.detectChanges();

    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(text).toContain('ABC1D23');
    expect(text).toContain('XYZ9A87');
    expect(emitted).toEqual(['next']);
  });
});

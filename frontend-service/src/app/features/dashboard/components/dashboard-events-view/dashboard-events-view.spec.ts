import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardEventsView } from './dashboard-events-view';

describe('DashboardEventsView', () => {
  let component: DashboardEventsView;
  let fixture: ComponentFixture<DashboardEventsView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardEventsView],
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardEventsView);
    component = fixture.componentInstance;
    component.events = [
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
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should normalize event results and render table rows', () => {
    expect(component.events.map((event) => event.resultNormalized)).toEqual([
      'authorized',
      'denied',
    ]);

    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(text).toContain('ABC1D23');
    expect(text).toContain('XYZ9A87');
  });
});

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
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

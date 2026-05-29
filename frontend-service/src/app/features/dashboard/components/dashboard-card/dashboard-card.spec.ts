import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardCard } from './dashboard-card';

@Component({
  standalone: true,
  template: '<span data-testid="icon"></span>',
})
class TestIcon {}

describe('DashboardCard', () => {
  let component: DashboardCard;
  let fixture: ComponentFixture<DashboardCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardCard],
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardCard);
    component = fixture.componentInstance;
    component.config = {
      title: 'Registered vehicles',
      value: 12,
      description: 'Registered vehicles in the system',
      icon: TestIcon,
      color: 'red',
    };
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render the configured metric', () => {
    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';

    expect(text).toContain('Registered vehicles');
    expect(text).toContain('12');
    expect(text).toContain('Registered vehicles in the system');
  });
});

import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScopesCard } from './scopes-card';

@Component({
  standalone: true,
  template: '<span data-testid="icon"></span>',
})
class TestIcon {}

describe('ScopesCard', () => {
  let component: ScopesCard;
  let fixture: ComponentFixture<ScopesCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScopesCard],
    }).compileComponents();

    fixture = TestBed.createComponent(ScopesCard);
    component = fixture.componentInstance;
    component.config = {
      id: 'scope-1',
      resource: 'vehicles',
      action: 'read',
      description: 'Read access to the resource',
      icon: TestIcon,
    };
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render the mapped scope data', () => {
    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';

    expect(text).toContain('vehicles');
    expect(text).toContain('read');
    expect(text).toContain('Read access to the resource');
  });
});

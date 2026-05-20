import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DirectionBadge } from './direction-badge';

describe('DirectionBadge', () => {
  let component: DirectionBadge;
  let fixture: ComponentFixture<DirectionBadge>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DirectionBadge],
    }).compileComponents();

    fixture = TestBed.createComponent(DirectionBadge);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

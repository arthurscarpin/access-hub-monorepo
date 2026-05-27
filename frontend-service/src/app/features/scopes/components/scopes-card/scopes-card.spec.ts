import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScopesCard } from './scopes-card';

describe('ScopesCard', () => {
  let component: ScopesCard;
  let fixture: ComponentFixture<ScopesCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScopesCard],
    }).compileComponents();

    fixture = TestBed.createComponent(ScopesCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

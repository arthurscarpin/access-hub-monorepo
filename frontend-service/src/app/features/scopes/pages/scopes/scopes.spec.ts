import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Scopes } from './scopes';

describe('Scopes', () => {
  let component: Scopes;
  let fixture: ComponentFixture<Scopes>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Scopes],
    }).compileComponents();

    fixture = TestBed.createComponent(Scopes);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Captures } from './captures';

describe('Captures', () => {
  let component: Captures;
  let fixture: ComponentFixture<Captures>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Captures],
    }).compileComponents();

    fixture = TestBed.createComponent(Captures);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

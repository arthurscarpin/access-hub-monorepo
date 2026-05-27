import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScopesPage } from './scopes-page';

describe('ScopesPage', () => {
  let component: ScopesPage;
  let fixture: ComponentFixture<ScopesPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScopesPage],
    }).compileComponents();

    fixture = TestBed.createComponent(ScopesPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

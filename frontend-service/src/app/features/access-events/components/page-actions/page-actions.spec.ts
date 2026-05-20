import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageActions } from './page-actions';

describe('PageActions', () => {
  let component: PageActions;
  let fixture: ComponentFixture<PageActions>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageActions],
    }).compileComponents();

    fixture = TestBed.createComponent(PageActions);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

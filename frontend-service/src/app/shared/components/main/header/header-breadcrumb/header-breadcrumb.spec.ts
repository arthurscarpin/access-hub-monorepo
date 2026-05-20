import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderBreadcrumb } from './header-breadcrumb';

describe('HeaderBreadcrumb', () => {
  let component: HeaderBreadcrumb;
  let fixture: ComponentFixture<HeaderBreadcrumb>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderBreadcrumb],
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderBreadcrumb);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

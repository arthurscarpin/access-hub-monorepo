import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebarLogo } from './sidebar-logo';

describe('SidebarLogo', () => {
  let component: SidebarLogo;
  let fixture: ComponentFixture<SidebarLogo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SidebarLogo],
    }).compileComponents();

    fixture = TestBed.createComponent(SidebarLogo);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

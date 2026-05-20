import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebarContent } from './sidebar-content';

describe('Sidebar', () => {
  let component: SidebarContent;
  let fixture: ComponentFixture<SidebarContent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SidebarContent],
    }).compileComponents();

    fixture = TestBed.createComponent(SidebarContent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

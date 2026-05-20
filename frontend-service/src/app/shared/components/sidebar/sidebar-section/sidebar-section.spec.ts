import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebarSection } from './sidebar-section';

describe('SidebarSection', () => {
  let component: SidebarSection;
  let fixture: ComponentFixture<SidebarSection>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SidebarSection],
    }).compileComponents();

    fixture = TestBed.createComponent(SidebarSection);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

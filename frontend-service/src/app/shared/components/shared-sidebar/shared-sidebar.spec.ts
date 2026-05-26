import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedSidebar } from './shared-sidebar';

describe('SharedSidebar', () => {
  let component: SharedSidebar;
  let fixture: ComponentFixture<SharedSidebar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedSidebar],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedSidebar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

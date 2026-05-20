import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebarUserCard } from './sidebar-user-card';

describe('SidebarUserCard', () => {
  let component: SidebarUserCard;
  let fixture: ComponentFixture<SidebarUserCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SidebarUserCard],
    }).compileComponents();

    fixture = TestBed.createComponent(SidebarUserCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

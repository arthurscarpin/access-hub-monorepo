import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccessEventsPage } from './access-events-page';

describe('AccessEventsPage', () => {
  let component: AccessEventsPage;
  let fixture: ComponentFixture<AccessEventsPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccessEventsPage],
    }).compileComponents();

    fixture = TestBed.createComponent(AccessEventsPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

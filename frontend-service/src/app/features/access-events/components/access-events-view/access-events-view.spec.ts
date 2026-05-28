import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccessEventsView } from './access-events-view';

describe('AccessEventsView', () => {
  let component: AccessEventsView;
  let fixture: ComponentFixture<AccessEventsView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccessEventsView],
    }).compileComponents();

    fixture = TestBed.createComponent(AccessEventsView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

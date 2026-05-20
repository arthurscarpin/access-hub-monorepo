import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TabButton } from './tab-button';

describe('TabButton', () => {
  let component: TabButton;
  let fixture: ComponentFixture<TabButton>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TabButton],
    }).compileComponents();

    fixture = TestBed.createComponent(TabButton);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedMenu } from './shared-menu';

describe('SharedMenu', () => {
  let component: SharedMenu;
  let fixture: ComponentFixture<SharedMenu>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedMenu],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedMenu);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

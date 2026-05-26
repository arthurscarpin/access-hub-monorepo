import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OwnersPage } from './owners-page';

describe('OwnersPage', () => {
  let component: OwnersPage;
  let fixture: ComponentFixture<OwnersPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OwnersPage],
    }).compileComponents();

    fixture = TestBed.createComponent(OwnersPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

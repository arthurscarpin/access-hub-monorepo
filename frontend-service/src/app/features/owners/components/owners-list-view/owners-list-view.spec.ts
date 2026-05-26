import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OwnersListView } from './owners-list-view';

describe('OwnersListView', () => {
  let component: OwnersListView;
  let fixture: ComponentFixture<OwnersListView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OwnersListView],
    }).compileComponents();

    fixture = TestBed.createComponent(OwnersListView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

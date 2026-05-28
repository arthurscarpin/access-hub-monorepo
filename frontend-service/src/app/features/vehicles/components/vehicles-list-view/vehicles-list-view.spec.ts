import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehiclesListView } from './vehicles-list-view';

describe('VehiclesListView', () => {
  let component: VehiclesListView;
  let fixture: ComponentFixture<VehiclesListView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VehiclesListView],
    }).compileComponents();

    fixture = TestBed.createComponent(VehiclesListView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

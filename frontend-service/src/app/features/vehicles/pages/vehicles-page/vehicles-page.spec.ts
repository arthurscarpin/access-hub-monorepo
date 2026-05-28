import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehiclesPage } from './vehicles-page';

describe('VehiclesPage', () => {
  let component: VehiclesPage;
  let fixture: ComponentFixture<VehiclesPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VehiclesPage],
    }).compileComponents();

    fixture = TestBed.createComponent(VehiclesPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';

import { Vehicle } from '../../../../core/models/vehicle.model';
import { VehicleService } from '../../../../core/services/vehicle.service';
import { paginatedFixture } from '../../../../testing/test-fixtures';
import { VehiclesListView } from './vehicles-list-view';

describe('VehiclesListView', () => {
  let component: VehiclesListView;
  let fixture: ComponentFixture<VehiclesListView>;
  let service: {
    vehicles: ReturnType<typeof signal<Vehicle[]>>;
    loading: ReturnType<typeof signal<boolean>>;
    totalElements: ReturnType<typeof signal<number>>;
    pagination: ReturnType<typeof signal<any>>;
    findAll: ReturnType<typeof vi.fn>;
    updateById: ReturnType<typeof vi.fn>;
    updateVehicleInList: ReturnType<typeof vi.fn>;
  };
  const vehicle: Vehicle = {
    id: 'vehicle-1',
    plate: 'ABC1D23',
    model: 'Civic',
    status: 'ACTIVE',
    ownerId: 'owner-1',
    ownerName: 'Maria Owner',
  };

  beforeEach(async () => {
    service = {
      vehicles: signal([vehicle]),
      loading: signal(false),
      totalElements: signal(1),
      pagination: signal(null),
      findAll: vi.fn(),
      updateById: vi.fn(),
      updateVehicleInList: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [VehiclesListView],
      providers: [{ provide: VehicleService, useValue: service }],
    }).compileComponents();

    fixture = TestBed.createComponent(VehiclesListView);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load vehicles on init and paginate inside bounds', () => {
    expect(service.findAll).toHaveBeenCalledWith();

    service.pagination.set(paginatedFixture([], { number: 2, first: false, last: false }));
    component.changePage('next');
    component.changePage('previous');

    expect(service.findAll).toHaveBeenCalledWith(3);
    expect(service.findAll).toHaveBeenCalledWith(1);
  });

  it('should update the vehicle list after status toggle succeeds', () => {
    service.updateById.mockReturnValue(of({ ...vehicle, status: 'INACTIVE' }));

    component.toggleStatus('vehicle-1');

    expect(service.updateById).toHaveBeenCalledWith('vehicle-1');
    expect(service.updateVehicleInList).toHaveBeenCalledWith({ ...vehicle, status: 'INACTIVE' });
  });

  it('should not update the list when status toggle fails', () => {
    service.updateById.mockReturnValue(throwError(() => new Error('failed')));

    component.toggleStatus('vehicle-1');

    expect(service.updateVehicleInList).not.toHaveBeenCalled();
  });
});

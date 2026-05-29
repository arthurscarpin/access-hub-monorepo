import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { Vehicle } from '../models/vehicle.model';
import { paginatedFixture } from '../../testing/test-fixtures';
import { VehicleService } from './vehicle.service';

describe('VehicleService', () => {
  let service: VehicleService;
  let http: HttpTestingController;

  const vehicle: Vehicle = {
    id: 'vehicle-1',
    plate: 'ABC1D23',
    model: 'Civic',
    status: 'ACTIVE',
    ownerId: 'owner-1',
    ownerName: 'Maria Owner',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(VehicleService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('loads vehicles and updates pagination signals', () => {
    service.findAll(3, 4);

    const req = http.expectOne((request) => request.url === `${environment.apiUrl}/vehicles`);
    expect(req.request.params.get('page')).toBe('3');
    expect(req.request.params.get('size')).toBe('4');
    req.flush(paginatedFixture([vehicle], { number: 3, size: 4, totalElements: 22 }));

    expect(service.vehicles()).toEqual([vehicle]);
    expect(service.pagination()?.number).toBe(3);
    expect(service.totalElements()).toBe(22);
  });

  it('posts a new vehicle and toggles status by id', () => {
    const payload = { plate: 'ABC1D23', model: 'Civic', ownerId: 'owner-1' };

    service.save(payload).subscribe();
    const postReq = http.expectOne(`${environment.apiUrl}/vehicles`);
    expect(postReq.request.method).toBe('POST');
    expect(postReq.request.body).toEqual(payload);
    postReq.flush(vehicle);

    service.updateById('vehicle-1').subscribe((updated) => expect(updated.status).toBe('INACTIVE'));
    const patchReq = http.expectOne(`${environment.apiUrl}/vehicles/vehicle-1`);
    expect(patchReq.request.method).toBe('PATCH');
    expect(patchReq.request.body).toEqual({});
    patchReq.flush({ ...vehicle, status: 'INACTIVE' });
  });

  it('updates a vehicle already loaded in the signal list', () => {
    service.findAll();
    http.expectOne(`${environment.apiUrl}/vehicles?page=0&size=5`).flush(paginatedFixture([vehicle]));

    service.updateVehicleInList({ ...vehicle, status: 'INACTIVE' });

    expect(service.vehicles()[0].status).toBe('INACTIVE');
  });
});

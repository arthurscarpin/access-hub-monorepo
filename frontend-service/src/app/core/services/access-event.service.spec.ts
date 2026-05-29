import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { AccessEvent } from '../models/access-event.model';
import { paginatedFixture } from '../../testing/test-fixtures';
import { AccessEventService } from './access-event.service';

describe('AccessEventService', () => {
  let service: AccessEventService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(AccessEventService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('loads access events and updates pagination signals', () => {
    const event: AccessEvent = {
      id: 'event-1',
      plate: 'ABC1D23',
      direction: 'IN',
      result: 'AUTHORIZED',
      timestamp: '2026-05-29T10:00:00Z',
    };

    service.findAll(1, 4);

    const req = http.expectOne((request) => request.url === `${environment.apiUrl}/access-events`);
    expect(req.request.params.get('page')).toBe('1');
    expect(req.request.params.get('size')).toBe('4');
    req.flush(paginatedFixture([event], { number: 1, size: 4, totalElements: 6 }));

    expect(service.accessEvents()).toEqual([event]);
    expect(service.pagination()?.number).toBe(1);
    expect(service.totalElements()).toBe(6);
  });

  it('clears access events when the request fails', () => {
    service.findAll();

    http.expectOne(`${environment.apiUrl}/access-events?page=0&size=5`).flush(
      { message: 'error' },
      { status: 500, statusText: 'Server Error' },
    );

    expect(service.accessEvents()).toEqual([]);
    expect(service.totalElements()).toBe(0);
  });
});

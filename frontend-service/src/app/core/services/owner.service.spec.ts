import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { Owner } from '../models/owner.model';
import { paginatedFixture } from '../../testing/test-fixtures';
import { OwnerService } from './owner.service';

describe('OwnerService', () => {
  let service: OwnerService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(OwnerService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('loads owners and updates pagination signals', () => {
    const owner: Owner = {
      id: 'owner-1',
      name: 'Maria Owner',
      email: 'maria@example.com',
      documentType: 'CPF',
      document: '12345678901',
    };

    service.findAll(1, 6);

    const req = http.expectOne((request) => request.url === `${environment.apiUrl}/owners`);
    expect(req.request.params.get('page')).toBe('1');
    expect(req.request.params.get('size')).toBe('6');
    req.flush(paginatedFixture([owner], { number: 1, size: 6, totalElements: 9 }));

    expect(service.owners()).toEqual([owner]);
    expect(service.pagination()?.number).toBe(1);
    expect(service.totalElements()).toBe(9);
  });

  it('clears owners when the request fails', () => {
    service.findAll();

    http.expectOne(`${environment.apiUrl}/owners?page=0&size=8`).flush(
      { message: 'error' },
      { status: 500, statusText: 'Server Error' },
    );

    expect(service.owners()).toEqual([]);
    expect(service.totalElements()).toBe(0);
  });

  it('posts a new owner', () => {
    const payload = {
      name: 'Maria Owner',
      email: 'maria@example.com',
      documentType: 'CPF' as const,
      document: '12345678901',
    };

    service.save(payload).subscribe((owner) => expect(owner.id).toBe('owner-1'));

    const req = http.expectOne(`${environment.apiUrl}/owners`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush({ id: 'owner-1', ...payload });
  });
});

import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { ScopeService } from './scope.service';

describe('ScopeService', () => {
  let service: ScopeService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(ScopeService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('loads scopes into the signal', () => {
    const scopes = [{ id: 'scope-1', name: 'vehicles:read' }];

    service.findAll();

    http.expectOne(`${environment.apiUrl}/scopes`).flush(scopes);

    expect(service.scopes()).toEqual(scopes);
  });

  it('clears scopes when the request fails', () => {
    service.findAll();

    http.expectOne(`${environment.apiUrl}/scopes`).flush(
      { message: 'error' },
      { status: 500, statusText: 'Server Error' },
    );

    expect(service.scopes()).toEqual([]);
  });
});

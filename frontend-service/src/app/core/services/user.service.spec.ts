import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { User } from '../models/user.model';
import { paginatedFixture } from '../../testing/test-fixtures';
import { UserService } from './users.service';

describe('UserService', () => {
  let service: UserService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(UserService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('loads users and updates pagination signals', () => {
    const user: User = {
      id: 'user-1',
      name: 'Admin',
      email: 'admin@accesshub.test',
      scopes: ['vehicles:read'],
    };

    service.findAll(2, 10);

    const req = http.expectOne((request) => request.url === `${environment.apiUrl}/users`);
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('page')).toBe('2');
    expect(req.request.params.get('size')).toBe('10');
    req.flush(paginatedFixture([user], { number: 2, size: 10, totalElements: 12 }));

    expect(service.users()).toEqual([user]);
    expect(service.pagination()?.number).toBe(2);
    expect(service.totalElements()).toBe(12);
    expect(service.loading()).toBe(false);
  });

  it('clears users when the request fails', () => {
    service.findAll();

    http.expectOne(`${environment.apiUrl}/users?page=0&size=8`).flush(
      { message: 'error' },
      { status: 500, statusText: 'Server Error' },
    );

    expect(service.users()).toEqual([]);
    expect(service.totalElements()).toBe(0);
    expect(service.loading()).toBe(false);
  });

  it('posts a new user', () => {
    const payload = {
      name: 'Admin',
      email: 'admin@accesshub.test',
      password: 'secret123',
      scopes: ['vehicles:read'],
    };

    service.save(payload).subscribe((user) => expect(user.id).toBe('user-1'));

    const req = http.expectOne(`${environment.apiUrl}/users`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush({ id: 'user-1', name: payload.name, email: payload.email, scopes: payload.scopes });
  });
});

import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScopeService } from '../../../../core/services/scope.service';
import { UserService } from '../../../../core/services/users.service';
import { paginatedFixture } from '../../../../testing/test-fixtures';
import { UsersListView } from './users-list-view';

describe('UsersListView', () => {
  let component: UsersListView;
  let fixture: ComponentFixture<UsersListView>;
  let userService: {
    users: ReturnType<typeof signal<any[]>>;
    loading: ReturnType<typeof signal<boolean>>;
    totalElements: ReturnType<typeof signal<number>>;
    pagination: ReturnType<typeof signal<any>>;
    findAll: ReturnType<typeof vi.fn>;
  };
  let scopeService: {
    scopes: ReturnType<typeof signal<any[]>>;
    findAll: ReturnType<typeof vi.fn>;
  };

  beforeEach(async () => {
    userService = {
      users: signal([
        {
          id: 'user-1',
          name: 'Admin',
          email: 'admin@accesshub.test',
          scopes: ['scope-read', 'scope-all', 'missing'],
        },
      ]),
      loading: signal(false),
      totalElements: signal(1),
      pagination: signal(null),
      findAll: vi.fn(),
    };
    scopeService = {
      scopes: signal([
        { id: 'scope-read', name: 'vehicles:read' },
        { id: 'scope-all', name: 'vehicles:all' },
      ]),
      findAll: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [UsersListView],
      providers: [
        { provide: UserService, useValue: userService },
        { provide: ScopeService, useValue: scopeService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UsersListView);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load users and scopes on init', () => {
    expect(scopeService.findAll).toHaveBeenCalledWith();
    expect(userService.findAll).toHaveBeenCalledWith();
  });

  it('should resolve and sort scopes by permission priority', () => {
    expect(component.usersWithScopes()[0].scopes.map((scope) => scope.name)).toEqual([
      'vehicles:all',
      'vehicles:read',
    ]);
  });

  it('should paginate inside bounds', () => {
    userService.pagination.set(paginatedFixture([], { number: 4, first: false, last: false }));

    component.changePage('next');
    component.changePage('previous');

    expect(userService.findAll).toHaveBeenCalledWith(5);
    expect(userService.findAll).toHaveBeenCalledWith(3);
  });
});

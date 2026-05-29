import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { AuthService } from '../../../../core/services/auth.service';
import { ScopeService } from '../../../../core/services/scope.service';
import { UserService } from '../../../../core/services/users.service';
import { WebsocketService } from '../../../../core/services/websocket.service';
import { UsersPage } from './users-page';

describe('UsersPage', () => {
  let component: UsersPage;
  let fixture: ComponentFixture<UsersPage>;
  let userService: any;

  beforeEach(async () => {
    userService = {
      users: signal([]),
      loading: signal(false),
      totalElements: signal(0),
      pagination: signal(null),
      findAll: vi.fn(),
      save: vi.fn().mockReturnValue(of({})),
    };

    await TestBed.configureTestingModule({
      imports: [UsersPage],
      providers: [
        provideRouter([]),
        { provide: UserService, useValue: userService },
        { provide: ScopeService, useValue: { scopes: signal([]), findAll: vi.fn() } },
        { provide: AuthService, useValue: { getLoginInfo: vi.fn().mockReturnValue({ email: '', username: '' }), logout: vi.fn() } },
        {
          provide: WebsocketService,
          useValue: {
            notifications: signal([]),
            unreadCount: signal(0),
            markAllAsRead: vi.fn(),
            markAsRead: vi.fn(),
            clearAll: vi.fn(),
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UsersPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle modal state and reload users', () => {
    component.openModal();
    expect(component.modalStage()).toBe(true);

    component.closeModal();
    component.reload();

    expect(component.modalStage()).toBe(false);
    expect(userService.findAll).toHaveBeenCalled();
  });
});

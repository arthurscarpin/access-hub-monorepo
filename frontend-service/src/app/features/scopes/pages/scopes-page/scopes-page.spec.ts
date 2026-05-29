import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { AuthService } from '../../../../core/services/auth.service';
import { ScopeService } from '../../../../core/services/scope.service';
import { WebsocketService } from '../../../../core/services/websocket.service';
import { ScopesPage } from './scopes-page';

describe('ScopesPage', () => {
  let component: ScopesPage;
  let fixture: ComponentFixture<ScopesPage>;
  let scopeService: { scopes: ReturnType<typeof signal<any[]>>; findAll: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    scopeService = {
      scopes: signal([{ id: 'scope-1', name: 'vehicle_access:write' }]),
      findAll: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [ScopesPage],
      providers: [
        provideRouter([]),
        { provide: ScopeService, useValue: scopeService },
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

    fixture = TestBed.createComponent(ScopesPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load and map scopes for cards', () => {
    expect(scopeService.findAll).toHaveBeenCalledWith();
    expect(component.scopesMapped()[0]).toMatchObject({
      id: 'scope-1',
      resource: 'vehicle access',
      action: 'write',
      description: 'Write access to the resource',
    });
  });
});

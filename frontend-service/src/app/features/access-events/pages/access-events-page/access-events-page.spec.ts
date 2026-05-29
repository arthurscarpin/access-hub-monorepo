import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { AccessEventService } from '../../../../core/services/access-event.service';
import { AuthService } from '../../../../core/services/auth.service';
import { WebsocketService } from '../../../../core/services/websocket.service';
import { paginatedFixture } from '../../../../testing/test-fixtures';
import { AccessEventsPage } from './access-events-page';

describe('AccessEventsPage', () => {
  let component: AccessEventsPage;
  let fixture: ComponentFixture<AccessEventsPage>;
  let accessEventService: {
    accessEvents: ReturnType<typeof signal<any[]>>;
    pagination: ReturnType<typeof signal<any>>;
    findAll: ReturnType<typeof vi.fn>;
  };

  beforeEach(async () => {
    accessEventService = {
      accessEvents: signal([]),
      pagination: signal(null),
      findAll: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [AccessEventsPage],
      providers: [
        provideRouter([]),
        { provide: AccessEventService, useValue: accessEventService },
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

    fixture = TestBed.createComponent(AccessEventsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should refresh and paginate access events', () => {
    expect(accessEventService.findAll).toHaveBeenCalledWith();

    accessEventService.pagination.set(paginatedFixture([], { number: 2, first: false, last: false }));

    component.handlePageChange('previous');
    component.handlePageChange('next');

    expect(accessEventService.findAll).toHaveBeenCalledWith(1);
    expect(accessEventService.findAll).toHaveBeenCalledWith(3);
  });
});

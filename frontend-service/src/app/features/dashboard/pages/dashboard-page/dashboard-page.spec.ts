import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { AccessEventService } from '../../../../core/services/access-event.service';
import { AuthService } from '../../../../core/services/auth.service';
import { OwnerService } from '../../../../core/services/owner.service';
import { VehicleService } from '../../../../core/services/vehicle.service';
import { WebsocketService } from '../../../../core/services/websocket.service';
import { DashboardPage } from './dashboard-page';

describe('DashboardPage', () => {
  let component: DashboardPage;
  let fixture: ComponentFixture<DashboardPage>;
  let vehicleService: any;
  let ownerService: any;
  let accessEventService: any;

  beforeEach(async () => {
    vehicleService = {
      vehicles: signal([]),
      loading: signal(false),
      totalElements: signal(7),
      pagination: signal(null),
      findAll: vi.fn(),
    };
    ownerService = {
      owners: signal([]),
      loading: signal(false),
      totalElements: signal(3),
      pagination: signal(null),
      findAll: vi.fn(),
    };
    accessEventService = {
      accessEvents: signal([]),
      totalElements: signal(12),
      pagination: signal(null),
      findAll: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [DashboardPage],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: { getLoginInfo: vi.fn().mockReturnValue({ email: 'admin@test.com', username: 'admin' }), logout: vi.fn() } },
        { provide: VehicleService, useValue: vehicleService },
        { provide: OwnerService, useValue: ownerService },
        { provide: AccessEventService, useValue: accessEventService },
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

    fixture = TestBed.createComponent(DashboardPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load dashboard totals on init and expose card configs', () => {
    expect(vehicleService.findAll).toHaveBeenCalledWith();
    expect(ownerService.findAll).toHaveBeenCalledWith();
    expect(accessEventService.findAll).toHaveBeenCalledWith(0, 4);

    expect(component.totalVehicles().value).toBe(7);
    expect(component.totalOwners().value).toBe(3);
    expect(component.totalAccessEvents().value).toBe(12);
    expect(component.headerConfig().title).toContain('Admin');
  });
});

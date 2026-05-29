import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { AuthService } from '../../../../core/services/auth.service';
import { VehicleService } from '../../../../core/services/vehicle.service';
import { WebsocketService } from '../../../../core/services/websocket.service';
import { VehiclesPage } from './vehicles-page';

describe('VehiclesPage', () => {
  let component: VehiclesPage;
  let fixture: ComponentFixture<VehiclesPage>;
  let vehicleService: any;

  beforeEach(async () => {
    vehicleService = {
      vehicles: signal([]),
      loading: signal(false),
      totalElements: signal(0),
      pagination: signal(null),
      findAll: vi.fn(),
      save: vi.fn().mockReturnValue(of({})),
      updateById: vi.fn().mockReturnValue(of({})),
      updateVehicleInList: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [VehiclesPage],
      providers: [
        provideRouter([]),
        { provide: VehicleService, useValue: vehicleService },
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

    fixture = TestBed.createComponent(VehiclesPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle modal state and reload vehicles', () => {
    component.openModal();
    expect(component.modalStage()).toBe(true);

    component.closeModal();
    component.reload();

    expect(component.modalStage()).toBe(false);
    expect(vehicleService.findAll).toHaveBeenCalled();
  });
});

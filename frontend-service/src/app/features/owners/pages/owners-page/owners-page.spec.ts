import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { AuthService } from '../../../../core/services/auth.service';
import { OwnerService } from '../../../../core/services/owner.service';
import { WebsocketService } from '../../../../core/services/websocket.service';
import { OwnersPage } from './owners-page';

describe('OwnersPage', () => {
  let component: OwnersPage;
  let fixture: ComponentFixture<OwnersPage>;
  let ownerService: any;

  beforeEach(async () => {
    ownerService = {
      owners: signal([]),
      loading: signal(false),
      totalElements: signal(0),
      pagination: signal(null),
      findAll: vi.fn(),
      save: vi.fn().mockReturnValue(of({})),
    };

    await TestBed.configureTestingModule({
      imports: [OwnersPage],
      providers: [
        provideRouter([]),
        { provide: OwnerService, useValue: ownerService },
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

    fixture = TestBed.createComponent(OwnersPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle modal state and reload owners', () => {
    component.openModal();
    expect(component.modalStage()).toBe(true);

    component.closeModal();
    component.reload();

    expect(component.modalStage()).toBe(false);
    expect(ownerService.findAll).toHaveBeenCalled();
  });
});

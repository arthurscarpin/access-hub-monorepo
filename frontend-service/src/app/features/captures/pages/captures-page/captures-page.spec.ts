import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { AuthService } from '../../../../core/services/auth.service';
import { CaptureService } from '../../../../core/services/capture.service';
import { WebsocketService } from '../../../../core/services/websocket.service';
import { CapturesPage } from './captures-page';

describe('CapturesPage', () => {
  let component: CapturesPage;
  let fixture: ComponentFixture<CapturesPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CapturesPage],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: { getLoginInfo: vi.fn().mockReturnValue({ email: '', username: '' }), logout: vi.fn() } },
        { provide: CaptureService, useValue: { upload: vi.fn().mockReturnValue(of({})) } },
        {
          provide: WebsocketService,
          useValue: {
            notifications: signal([]),
            unreadCount: signal(0),
            markAllAsRead: vi.fn(),
            markAsRead: vi.fn(),
            clearAll: vi.fn(),
            initializeCaptureSubscription: vi.fn(),
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CapturesPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle modal state', () => {
    component.openModal();
    expect(component.modalStage()).toBe(true);

    component.closeModal();
    expect(component.modalStage()).toBe(false);
  });
});

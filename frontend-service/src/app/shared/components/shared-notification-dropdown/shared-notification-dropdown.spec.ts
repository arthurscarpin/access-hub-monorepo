import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebsocketService } from '../../../core/services/websocket.service';
import { NotificationItem } from '../../../core/models/ capture.model';
import { SharedNotificationDropdown } from './shared-notification-dropdown';

describe('SharedNotificationDropdown', () => {
  let component: SharedNotificationDropdown;
  let fixture: ComponentFixture<SharedNotificationDropdown>;
  let websocketService: {
    notifications: ReturnType<typeof signal<NotificationItem[]>>;
    unreadCount: ReturnType<typeof signal<number>>;
    markAllAsRead: ReturnType<typeof vi.fn>;
    markAsRead: ReturnType<typeof vi.fn>;
    clearAll: ReturnType<typeof vi.fn>;
  };

  beforeEach(async () => {
    websocketService = {
      notifications: signal<NotificationItem[]>([
        {
          id: 'capture-1_123',
          title: 'Capture Completed: ABC1D23',
          description: 'AI has successfully consolidated the final plate results.',
          type: 'success' as const,
          unread: true,
          timestamp: 123,
          reasoning: 'Two images matched the same plate.',
          isExpanded: false,
        },
      ]),
      unreadCount: signal(1),
      markAllAsRead: vi.fn(),
      markAsRead: vi.fn(),
      clearAll: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [SharedNotificationDropdown],
      providers: [{ provide: WebsocketService, useValue: websocketService }],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedNotificationDropdown);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render notifications and delegate read actions', () => {
    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';

    expect(text).toContain('Capture Completed: ABC1D23');
    expect(text).toContain('1');

    component.markAsRead('capture-1_123');
    component.markAllAsRead();
    component.clearAll();

    expect(websocketService.markAsRead).toHaveBeenCalledWith('capture-1_123');
    expect(websocketService.markAllAsRead).toHaveBeenCalled();
    expect(websocketService.clearAll).toHaveBeenCalled();
  });

  it('should expand reasoning without propagating the click', () => {
    const event = { stopPropagation: vi.fn() } as unknown as Event;

    component.toggleReasoning('capture-1_123', event);

    expect(event.stopPropagation).toHaveBeenCalled();
    expect(websocketService.notifications()[0].isExpanded).toBe(true);
  });
});

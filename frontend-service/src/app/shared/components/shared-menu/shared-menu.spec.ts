import { ComponentFixture, TestBed } from '@angular/core/testing';
import { signal } from '@angular/core';

import { SharedMenu } from './shared-menu';
import { WebsocketService } from '../../../core/services/websocket.service';

describe('SharedMenu', () => {
  let component: SharedMenu;
  let fixture: ComponentFixture<SharedMenu>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedMenu],
      providers: [
        {
          provide: WebsocketService,
          useValue: {
            notifications: signal([]),
            unreadCount: signal(0).asReadonly(),
            markAllAsRead: vi.fn(),
            markAsRead: vi.fn(),
            clearAll: vi.fn(),
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedMenu);
    component = fixture.componentInstance;
    component.config = { category: 'Operation', title: 'Dashboard' };
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle the notification dropdown and update the badge count', () => {
    expect(component.dropdownStage()).toBe(false);

    component.toggleDropdown();
    component.updateCount(3);
    fixture.detectChanges();

    expect(component.dropdownStage()).toBe(true);
    expect(component.notificationCount()).toBe(3);
    expect((fixture.nativeElement as HTMLElement).textContent).toContain('3');
  });
});

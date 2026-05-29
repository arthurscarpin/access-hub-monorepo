import { TestBed } from '@angular/core/testing';
import { Client, IMessage } from '@stomp/stompjs';

import { WebsocketService } from './websocket.service';

type SubscriptionCallback = (message: IMessage) => void;

describe('WebsocketService', () => {
  let service: WebsocketService;
  let activateSpy: ReturnType<typeof vi.spyOn>;
  let deactivateSpy: ReturnType<typeof vi.spyOn>;
  let subscribeSpy: ReturnType<typeof vi.spyOn>;
  let unsubscribeSpy: any;

  beforeEach(() => {
    unsubscribeSpy = vi.fn();
    activateSpy = vi.spyOn(Client.prototype, 'activate').mockImplementation(vi.fn());
    deactivateSpy = vi.spyOn(Client.prototype, 'deactivate').mockImplementation(vi.fn());
    subscribeSpy = vi
      .spyOn(Client.prototype, 'subscribe')
      .mockReturnValue({
        id: 'subscription-1',
        unsubscribe: () => {
          unsubscribeSpy();
        },
      });

    TestBed.configureTestingModule({});
    service = TestBed.inject(WebsocketService);
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('activates the STOMP client when created', () => {
    expect(activateSpy).toHaveBeenCalled();
  });

  it('subscribes to the active capture when the client connects', () => {
    service.initializeCaptureSubscription('capture-1');

    expect(subscribeSpy).not.toHaveBeenCalled();

    (service as any).stompClient.onConnect();

    expect(subscribeSpy).toHaveBeenCalledWith('/topic/capture/capture-1', expect.any(Function));
  });

  it('creates unread notifications from incoming capture events', () => {
    Object.defineProperty((service as any).stompClient, 'connected', {
      value: true,
      configurable: true,
    });

    service.initializeCaptureSubscription('capture-1');

    const callback = subscribeSpy.mock.calls[0][1] as SubscriptionCallback;
    callback({
      body: JSON.stringify({
        captureId: 'capture-1',
        type: 'AI_NOTIFICATION',
        timestamp: 123,
        payload: {
          id: 'capture-1',
          images: [],
          status: 'COMPLETED',
          direction: 'IN',
          finalPlate: 'ABC1D23',
          finalConfidence: 0.95,
          reasoning: 'matching images',
          processedImagesCount: 2,
        },
      }),
    } as IMessage);

    expect(service.notifications()[0]).toMatchObject({
      id: 'capture-1_123',
      title: 'Capture Completed: ABC1D23',
      type: 'success',
      unread: true,
      reasoning: 'matching images',
    });
    expect(service.unreadCount()).toBe(1);
  });

  it('marks notifications as read and clears them', () => {
    service.notifications.set([
      {
        id: 'notification-1',
        title: 'Processing',
        description: 'Working',
        type: 'processing',
        unread: true,
        timestamp: 123,
      },
    ]);

    service.markAsRead('notification-1');
    expect(service.unreadCount()).toBe(0);

    service.notifications.set([{ ...service.notifications()[0], unread: true }]);
    service.markAllAsRead();
    expect(service.unreadCount()).toBe(0);

    service.clearAll();
    expect(service.notifications()).toEqual([]);
  });

  it('unsubscribes and deactivates on destroy', () => {
    Object.defineProperty((service as any).stompClient, 'connected', {
      value: true,
      configurable: true,
    });
    service.initializeCaptureSubscription('capture-1');

    service.ngOnDestroy();

    expect(unsubscribeSpy).toHaveBeenCalled();
    expect(deactivateSpy).toHaveBeenCalled();
  });
});

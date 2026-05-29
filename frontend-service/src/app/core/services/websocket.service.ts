import { Injectable, OnDestroy, signal, computed } from '@angular/core';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import { CaptureRealtimeEvent, NotificationItem } from '../models/ capture.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class WebsocketService implements OnDestroy {
  private stompClient: Client;
  private currentSubscription?: StompSubscription;
  private activeCaptureId?: string;

  public notifications = signal<NotificationItem[]>([]);

  public unreadCount = computed(() => {
    return this.notifications().filter((notif) => notif.unread).length;
  });

  constructor() {
    this.stompClient = new Client({
      brokerURL: environment.webSocketUrl,
      reconnectDelay: 5000,
    });

    this.stompClient.onConnect = () => {
      console.log('Connected to STOMP Broker!');
      
      if (this.activeCaptureId) {
        this.subscribeToTopic(this.activeCaptureId);
      }
    };

    this.stompClient.activate();
  }

  public initializeCaptureSubscription(captureId: string): void {
    this.activeCaptureId = captureId;

    if (this.stompClient.connected) {
      this.subscribeToTopic(captureId);
    }
  }

  private subscribeToTopic(captureId: string): void {
    if (this.currentSubscription) {
      this.currentSubscription.unsubscribe();
      console.log('Previous channel subscription successfully canceled.');
    }

    console.log(`Opening dynamic channel for monitoring: /topic/capture/${captureId}`);
    
    this.currentSubscription = this.stompClient.subscribe(
      `/topic/capture/${captureId}`, 
      (message: IMessage) => {
        try {
          const event: CaptureRealtimeEvent = JSON.parse(message.body);
          this.processIncomingEvent(event);
        } catch (error) {
          console.error('Error parsing event received via WebSocket:', error);
        }
      }
    );
  }

  private processIncomingEvent(event: CaptureRealtimeEvent): void {
    const payload = event.payload;
    let title = '';
    let description = '';
    let type: 'processing' | 'success' | 'info' = 'info';

    if (event.type === 'OCR_NOTIFICATION' || event.type === 'IMAGE_PROCESSED') {
      title = `Capture Processing`;
      description = `Analyzing license plate images... Progress: ${payload.processedImagesCount}/${payload.images.length}`;
      type = 'processing';
    } else if (event.type === 'AI_NOTIFICATION' && payload.status === 'COMPLETED') {
      title = `Capture Completed: ${payload.finalPlate}`;
      description = `AI has successfully consolidated the final plate results.`;
      type = 'success';
    }

    const newNotification: NotificationItem = {
      id: `${payload.id}_${event.timestamp}`,
      title,
      description,
      type,
      unread: true,
      timestamp: event.timestamp,
      reasoning: payload.reasoning, 
      isExpanded: false, 
    };

    this.notifications.update((oldList) => [newNotification, ...oldList]);
  }

  public markAllAsRead(): void {
    this.notifications.update((oldList) => oldList.map((notif) => ({ ...notif, unread: false })));
  }

  public markAsRead(id: string): void {
    this.notifications.update((oldList) =>
      oldList.map((notif) => (notif.id === id ? { ...notif, unread: false } : notif)),
    );
  }

  public clearAll(): void {
    this.notifications.set([]);
  }

  ngOnDestroy(): void {
    if (this.currentSubscription) {
      this.currentSubscription.unsubscribe();
    }
    this.stompClient.deactivate();
  }
}
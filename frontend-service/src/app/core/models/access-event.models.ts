export interface AccessEvent {
  id: string;
  plate: string;
  timestamp: string;
  direction: string;
  result: string;
}

export interface AccessEventsResponse {
  content: AccessEvent[];
}
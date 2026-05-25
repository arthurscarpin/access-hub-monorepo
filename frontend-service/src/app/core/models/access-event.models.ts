export interface AccessEventResponse {
  id: string;
  plate: string;
  direction: 'Entry' | 'Exit';
  status: 'Granted' | 'Denied';
  date: string;
}
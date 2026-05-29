export interface Capture {
  id: string;
  captureStatus: string;
  message: string;
  extractedFiles: string[];
}

export interface CaptureImage {
  id: string;
  filename: string;
  status: 'RECEIVED' | 'PROCESSING' | 'COMPLETED' | 'ERROR';
  ocr: any[];
  timestamp: string;
}

export interface CapturePayload {
  id: string;
  images: CaptureImage[];
  status: 'RECEIVED' | 'PROCESSING' | 'COMPLETED';
  direction: string;
  finalPlate: string | null;
  finalConfidence: number | null;
  reasoning: string | null;
  processedImagesCount: number;
}

export interface CaptureRealtimeEvent {
  captureId: string;
  type: 'OCR_NOTIFICATION' | 'AI_NOTIFICATION' | 'IMAGE_PROCESSED';
  payload: CapturePayload;
  timestamp: number;
}

export interface NotificationItem {
  id: string;
  title: string;
  description: string;
  type: 'processing' | 'success' | 'info';
  unread: boolean;
  timestamp: number;
  reasoning?: string | null;
  isExpanded?: boolean; 
}
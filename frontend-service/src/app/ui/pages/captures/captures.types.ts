import {
  LucideFileArchive,
  LucideArrowDownLeft,
  LucideArrowUpRight,
} from '@lucide/angular';

export interface ProcessingHistory {
  id: string;
  fileName: string;
  direction: 'Entry' | 'Exit';
  directionIcon: any;
  imagesProcessed: number;
  filesCount: number;
  status: 'Processed' | 'Processing' | 'Failed';
  icon: any;
}

export const PROCESSING_HISTORY_OPTIONS: ProcessingHistory[] = [
  {
    id: 'b2f1c9a0-3d8e-4c7a-9a1d-1e2f3a4b5c6d',
    fileName: 'batch-entry-01.zip',
    direction: 'Entry',
    directionIcon: LucideArrowDownLeft,
    imagesProcessed: 10,
    filesCount: 10,
    status: 'Processed',
    icon: LucideFileArchive,
  },
  {
    id: 'c3a2d8f1-7b9e-4a1c-8d2f-3a4b5c6d7e8f',
    fileName: 'batch-exit-01.zip',
    direction: 'Exit',
    directionIcon: LucideArrowUpRight,
    imagesProcessed: 8,
    filesCount: 9,
    status: 'Processed',
    icon: LucideFileArchive,
  },
  {
    id: 'd4e5f6a7-1b2c-4d3e-9f8a-5b6c7d8e9f01',
    fileName: 'batch-exit-02.zip',
    direction: 'Exit',
    directionIcon: LucideArrowUpRight,
    imagesProcessed: 6,
    filesCount: 7,
    status: 'Failed',
    icon: LucideFileArchive,
  }
];
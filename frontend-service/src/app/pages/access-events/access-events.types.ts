import { LucideArrowDownLeft, LucideArrowUpRight } from '@lucide/angular';
import { Type } from "@angular/core"

export interface AccessEvents {
    id: string;
    plate: string;
    direction: string;
    status: 'Granted' | 'Denied';
    date: string;
    icon: Type<any>;
}

export const ACCESS_EVENTS_OPTIONS: AccessEvents[] = [
  {
    id: 'e2329c14-a826-4e98-868a-e0ba916307e4',
    plate: 'ABC1D23',
    direction: 'Entry',
    status: 'Granted',
    date: '2026-05-17T19:39:50.440166Z',
    icon: LucideArrowDownLeft
  },
  {
    id: 'a4d67f12-91fd-4b7c-9cb1-8f52d5c67121',
    plate: 'XYZ9K87',
    direction: 'Exit',
    status: 'Denied',
    date: '2026-05-17T19:39:50.440166Z',
    icon: LucideArrowUpRight
  },
  {
    id: 'c6b8d1f4-58e0-4c0f-b6e1-3d71c2a9f441',
    plate: 'BRA2A11',
    direction: 'Entry',
    status: 'Granted',
    date: '2026-05-17T19:39:50.440166Z',
    icon: LucideArrowDownLeft
  },
  {
    id: 'f8a9c7d3-2f11-4f2e-a5c9-6e7d4b0d9a55',
    plate: 'CAR7F55',
    direction: 'Exit',
    status: 'Granted',
    date: '2026-05-17T19:39:50.440166Z',
    icon: LucideArrowUpRight
  },
  {
    id: 'b5d3a1f7-7f9c-4f44-8c1a-9a8f2c7e3342',
    plate: 'DDD4K21',
    direction: 'Entry',
    status: 'Denied',
    date: '2026-05-17T19:39:50.440166Z',
    icon: LucideArrowDownLeft
  },
  {
    id: 'e2329c14-a826-4e98-868a-e0ba916307e4',
    plate: 'ABC1D23',
    direction: 'Entry',
    status: 'Granted',
    date: '2026-05-17T19:39:50.440166Z',
    icon: LucideArrowDownLeft
  },
  {
    id: 'a4d67f12-91fd-4b7c-9cb1-8f52d5c67121',
    plate: 'XYZ9K87',
    direction: 'Exit',
    status: 'Denied',
    date: '2026-05-17T19:39:50.440166Z',
    icon: LucideArrowUpRight
  },
  {
    id: 'c6b8d1f4-58e0-4c0f-b6e1-3d71c2a9f441',
    plate: 'BRA2A11',
    direction: 'Entry',
    status: 'Granted',
    date: '2026-05-17T19:39:50.440166Z',
    icon: LucideArrowDownLeft
  }
];
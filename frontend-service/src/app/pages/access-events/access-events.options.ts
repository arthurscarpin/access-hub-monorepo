import { AccessEvents } from '@pages/access-events/access-events.interface';
import { LucideArrowDownLeft, LucideArrowUpRight } from '@lucide/angular';


export const ACCESS_EVENTS_OPTIONS: AccessEvents[] = [
  {
    id: 'e2329c14-a826-4e98-868a-e0ba916307e4',
    plate: 'ABC1D23',
    direction: 'Entry',
    status: 'Granted',
    date: '20/05, 12:51',
    icon: LucideArrowDownLeft
  },
  {
    id: 'a4d67f12-91fd-4b7c-9cb1-8f52d5c67121',
    plate: 'XYZ9K87',
    direction: 'Exit',
    status: 'Denied',
    date: '20/05, 13:14',
    icon: LucideArrowUpRight
  },
  {
    id: 'c6b8d1f4-58e0-4c0f-b6e1-3d71c2a9f441',
    plate: 'BRA2A11',
    direction: 'Entry',
    status: 'Granted',
    date: '20/05, 14:32',
    icon: LucideArrowDownLeft
  },
  {
    id: 'f8a9c7d3-2f11-4f2e-a5c9-6e7d4b0d9a55',
    plate: 'CAR7F55',
    direction: 'Exit',
    status: 'Granted',
    date: '20/05, 15:08',
    icon: LucideArrowUpRight
  },
  {
    id: 'b5d3a1f7-7f9c-4f44-8c1a-9a8f2c7e3342',
    plate: 'DDD4K21',
    direction: 'Entry',
    status: 'Denied',
    date: '20/05, 16:27',
    icon: LucideArrowDownLeft
  }
];
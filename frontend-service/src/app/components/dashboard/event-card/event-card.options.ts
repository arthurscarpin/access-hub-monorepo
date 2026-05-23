import { LucideActivity, LucideShieldBan, LucideCar, LucideDownload } from '@lucide/angular';
import { EventCard } from './event-card.interface';

export const EVENT_CARD_OPTIONS: EventCard[] = [
  {
    title: 'Authorized access',
    value: 6,
    percentage: '12.4%',
    description: 'Last 24 hours',
    icon: LucideActivity,
  },
  {
    title: 'Unauthorized access',
    value: 18,
    percentage: '8.1%',
    description: 'Last 24 hours',
    icon: LucideShieldBan,
  },
  {
    title: 'Active vehicles',
    value: 42,
    percentage: '5.2',
    description: 'Registered',
    icon: LucideCar,
  },
  {
    title: 'Captures in processing',
    value: 12,
    percentage: '4.7',
    description: 'Processing now',
    icon: LucideDownload,
  },
];

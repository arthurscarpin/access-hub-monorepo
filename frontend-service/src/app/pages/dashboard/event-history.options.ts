import { EventHistoryItem } from "./event-history.interface";
import { LucideArrowUpRight, LucideArrowDownLeft } from "@lucide/angular";

export const EVENT_HISTORY_OPTIONS: EventHistoryItem[] = [
  {
    plate: 'ABC1D23',
    vehicle: 'Toyota Corolla',
    status: 'granted',
    icon: LucideArrowUpRight
  },
  {
    plate: 'XYZ9K87',
    vehicle: 'Honda Civic',
    status: 'denied',
    icon: LucideArrowDownLeft
  },
  {
    plate: 'BRA2A11',
    vehicle: 'BMW X1',
    status: 'granted',
    icon: LucideArrowUpRight
  },
  {
    plate: 'XYZ9K87',
    vehicle: 'BMW X1',
    status: 'denied',
    icon: LucideArrowUpRight
  },
  {
    plate: 'ABC1D23',
    vehicle: 'Honda Civic',
    status: 'granted',
    icon: LucideArrowUpRight
  }
];
import { SidebarGroup, SidebarIconKey } from '@components/shared/sidebar/sidebar.types';

import {
  LucideLayoutDashboard,
  LucideActivity,
  LucideUpload,
  LucideCar,
  LucideCircleUser,
  LucideUsers,
  LucideShield,
} from '@lucide/angular';

export const SIDEBAR_ICON_MAP: Record<SidebarIconKey, any> = {
  dashboard: LucideLayoutDashboard,
  activity: LucideActivity,
  upload: LucideUpload,
  car: LucideCar,
  owner: LucideCircleUser,
  users: LucideUsers,
  shield: LucideShield,
};

export const SIDEBAR_MENU: SidebarGroup[] = [
  {
    group: 'Operation',
    items: [
      { label: 'Dashboard', route: '/dashboard', icon: 'dashboard' },
      { label: 'Access Events', route: '/access-events', icon: 'activity' },
      { label: 'Captures', route: '/captures', icon: 'upload' },
    ],
  },
  {
    group: 'Management',
    items: [
      { label: 'Vehicles', route: '/vehicles', icon: 'car' },
      { label: 'Owners', route: '/owners', icon: 'owner' },
      { label: 'Users', route: '/users', icon: 'users' },
      { label: 'Scopes', route: '/scopes', icon: 'shield' },
    ],
  },
];

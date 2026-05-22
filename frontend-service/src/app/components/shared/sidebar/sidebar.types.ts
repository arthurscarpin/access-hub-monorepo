export type SidebarIconKey =
  | 'dashboard'
  | 'activity'
  | 'upload'
  | 'car'
  | 'owner'
  | 'users'
  | 'shield';

export interface SidebarItem {
  label: string;
  route: string;
  icon: SidebarIconKey;
}

export interface SidebarGroup {
  group: string;
  items: SidebarItem[];
}
import { LucideShieldCheck, LucideBookOpen, LucidePencilLine } from '@lucide/angular';
import { Type } from "@angular/core";

export interface Scopes {
    resource: string;
    action: string;
    description: string;
    icon: Type<any>;
}

export const SCOPES: Scopes[] = [
  {
    resource: 'admin',
    action: 'all',
    description: "Full access to the platform and settings.",
    icon: LucideShieldCheck
  },
  {
    resource: 'vehicle',
    action: 'write',
    description: "Write access to the resource",
    icon: LucidePencilLine
  },
  {
    resource: 'vehicle',
    action: 'read',
    description: "Read access to the resource",
    icon: LucideBookOpen
  },
  {
    resource: 'owner',
    action: 'write',
    description: "Write access to the resource",
    icon: LucidePencilLine
  },
  {
    resource: 'owner',
    action: 'read',
    description: "Read access to the resource",
    icon: LucideBookOpen
  },
  {
    resource: 'access_event',
    action: 'write',
    description: "Write access to the resource",
    icon: LucidePencilLine
  },
  {
    resource: 'access_event',
    action: 'read',
    description: "Read access to the resource",
    icon: LucideBookOpen
  },
  {
    resource: 'capture',
    action: 'write',
    description: "Write access to the resource",
    icon: LucidePencilLine
  },
  {
    resource: 'capture',
    action: 'read',
    description: "Read access to the resource",
    icon: LucideBookOpen
  }
];

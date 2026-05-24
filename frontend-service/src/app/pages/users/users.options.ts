import { Users } from "@pages/users/users.interface";

export const USERS_OPTIONS: Users[] = [
  {
    id: '1f3c2d8a-4a1e-4f6b-9d2a-1c9e8b7f0a11',
    name: 'Admin',
    email: 'admin@admin.com',
    scopes: ['admin:all'],
  },
  {
    id: '2a7d9f10-6c3b-4c9a-8d11-0f2e9b3c7a21',
    name: 'Manager',
    email: 'manager@company.com',
    scopes: ['vehicle:write', 'vehicle:read'],
  },
  {
    id: '3b8e1c22-9f4d-4a10-8c33-4e9a2f1d6b32',
    name: 'Owner Admin',
    email: 'owner@company.com',
    scopes: ['owner:read', 'owner:write'],
  },
  {
    id: '4c9f2d33-1a5e-4b21-9d44-7f1b3c2d8e43',
    name: 'Access User',
    email: 'access@company.com',
    scopes: ['access_event:read'],
  },
  {
    id: '5d0a3e44-2b6f-4c32-8e55-9a2c4d1e7f54',
    name: 'Capture Operator',
    email: 'capture@company.com',
    scopes: ['capture:read', 'capture:write'],
  },
];
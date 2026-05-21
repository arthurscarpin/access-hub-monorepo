import { Routes } from '@angular/router';
import { Login } from './features/login/pages/login/login';
import { Dashboard } from './features/dashboard/pages/dashaboard/dashboard';
import { AccessEvents } from './features/access-events/pages/access-events/access-events';
import { Captures } from './features/captures/pages/captures/captures';
import { Vehicles } from './features/vehicles/pages/vehicles/vehicles';
import { Owners } from './features/owners/pages/owners/owners';
import { UsersControl } from './features/users/pages/users/users';
import { Scopes } from './features/scopes/pages/scopes/scopes';

export const routes: Routes = [
  {
    path: 'login',
    component: Login
  },
  {
    path: 'dashboard',
    component: Dashboard
  },
  {
    path: 'access-events',
    component: AccessEvents
  },
  {
    path: 'captures',
    component: Captures
  },
  {
    path: 'vehicles',
    component: Vehicles
  },
  {
    path: 'owners',
    component: Owners
  },
  {
    path: 'users',
    component: UsersControl
  },
  {
    path: 'scopes',
    component: Scopes
  }
];

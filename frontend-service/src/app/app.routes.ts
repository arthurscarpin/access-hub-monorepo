import { Routes } from '@angular/router';
import { Login } from './features/auth/pages/login/login';
import { Home } from './features/dashboard/pages/home/home';
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
    component: Home
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

import { Routes } from '@angular/router';
import { Login } from '@pages/login/login';
import { Dashboard } from '@pages/dashboard/dashboard';
import { AccessEvents } from '@pages/access-events/access-events';
import { Captures } from '@pages/captures/captures';
import { Vehicles } from '@pages/vehicles/vehicles';
import { Owners } from '@pages/owners/owners';
import { Users } from '@pages/users/users';
import { Scopes } from '@pages/scopes/scopes';
import { authGuard } from '@core/auth/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: Login,
  },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        component: Dashboard,
      },
      {
        path: 'scopes',
        component: Scopes,
      },
      {
        path: 'access-events',
        component: AccessEvents,
      },
      {
        path: 'captures',
        component: Captures,
      },
      {
        path: 'vehicles',
        component: Vehicles,
      },
      {
        path: 'owners',
        component: Owners,
      },
      {
        path: 'users',
        component: Users,
      },
    ],
  },
];

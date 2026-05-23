import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Dashboard } from '@pages/dashboard/dashboard';
import { AccessEvents } from './features/access-events/pages/access-events/access-events';
import { Captures } from './features/captures/pages/captures/captures';
import { Vehicles } from './features/vehicles/pages/vehicles/vehicles';
import { Owners } from './features/owners/pages/owners/owners';
import { UsersControl } from './features/users/pages/users/users';
import { Scopes } from './features/scopes/pages/scopes/scopes';
import { authGuard } from './core/auth/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: Login
  },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        component: Dashboard
      },
      // {
      //   path: 'access-events',
      //   component: AccessEvents
      // },
      // {
      //   path: 'captures',
      //   component: Captures
      // },
      // {
      //   path: 'vehicles',
      //   component: Vehicles
      // },
      // {
      //   path: 'owners',
      //   component: Owners
      // },
      // {
      //   path: 'users',
      //   component: UsersControl
      // },
      // {
      //   path: 'scopes',
      //   component: Scopes
      // }
    ]
  }
];

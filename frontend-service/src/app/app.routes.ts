import { Routes } from '@angular/router';
import { Login } from 'src/app/ui/pages/login/login';
import { Dashboard } from 'src/app/ui/pages/dashboard/dashboard';
import { AccessEvents } from 'src/app/ui/pages/access-events/access-events';
import { Captures } from 'src/app/ui/pages/captures/captures';
import { Vehicles } from 'src/app/ui/pages/vehicles/vehicles';
import { Owners } from 'src/app/ui/pages/owners/owners';
import { Users } from 'src/app/ui/pages/users/users';
import { Scopes } from 'src/app/ui/pages/scopes/scopes';
import { authGuard } from '@config/auth.guard';

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

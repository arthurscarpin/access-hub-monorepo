import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { LoginPage } from './features/login/pages/login-page/login-page';
import { OwnersPage } from './features/owners/pages/owners-page/owners-page';
import { ScopesPage } from './features/scopes/pages/scopes-page/scopes-page';
import { UsersPage } from './features/users/pages/users-page/users-page';
import { VehiclesPage } from './features/vehicles/pages/vehicles-page/vehicles-page';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginPage,
  },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      {
        path: 'owners',
        component: OwnersPage,
      },
      {
        path: 'scopes',
        component: ScopesPage,
      },
      {
        path: 'users',
        component: UsersPage,
      },
      {
        path: 'vehicles',
        component: VehiclesPage,
      }
    ],
  },
];

import { Routes } from '@angular/router';
import { OwnersPage } from './features/owners/pages/owners-page/owners-page';
import { LoginPage } from './features/login/pages/login-page/login-page';
import { authGuard } from './core/guards/auth.guard';

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
    ],
  },
];

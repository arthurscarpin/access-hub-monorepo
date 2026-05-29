import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const isLogin = request.url.includes('/login');
  const isScopes = request.url.includes('/scopes');

  const isCreateUser = request.url.includes('/users') && request.method === 'POST';

  if (isLogin || isScopes || isCreateUser) {
    return next(request);
  }

  const service = inject(AuthService);
  const token = service.getToken();

  let requestClone = request.clone({
    setHeaders: { Authorization: `Bearer ${token}` },
  });
  return next(requestClone).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        service.logout();
      }
      return throwError(() => error);
    }),
  );
};

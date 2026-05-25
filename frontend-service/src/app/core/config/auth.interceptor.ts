import { HttpInterceptorFn, HttpErrorResponse } from "@angular/common/http";
import { catchError, throwError } from 'rxjs';
import { inject } from "@angular/core";
import { AuthService } from "@core/services/auth.service";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.url.includes('/login') || req.url.includes('/users') || req.url.includes('/scopes')) {
    return next(req);
  }
  const authService = inject(AuthService);
  const token = authService.getToken();
  
  let clonedReq = req;
  if (token) {
    clonedReq = req.clone({
      setHeaders: { 
        Authorization: `Bearer ${token}` 
      }
    });
  }
  return next(clonedReq).pipe(
    catchError((error: HttpErrorResponse) => {

      if (error.status === 401) {
        authService.logout();
      }
      return throwError(() => error);
    })
  );
};
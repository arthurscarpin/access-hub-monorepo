import { HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { AuthService } from "@core/services/auth.service";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.url.includes('/login') || req.url.includes('/users') || req.url.includes('/scopes')) {
    return next(req);
  }
  const authService = inject(AuthService);
  const token = authService.getToken();
  if (token) {
    const cloneReq = req.clone({
      setHeaders: { 
        Authorization: `Bearer ${token}` 
      }
    });
    return next(cloneReq);
  }
  return next(req);
};
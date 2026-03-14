import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      console.error('HTTP Error:', {
        status: error.status,
        url: req.url,
        message: error.message
      });

      // Handle 401 Unauthorized - token expired
      if (error.status === 401 && !req.url.includes('/auth/refresh') && !req.url.includes('/auth/login')) {
        console.log('Token expired, attempting refresh...');
        // Try to refresh token
        return authService.refreshToken().pipe(
          switchMap((response) => {
            console.log('Token refreshed successfully');
            // Retry the original request with new token
            const newReq = req.clone({
              setHeaders: {
                Authorization: `Bearer ${response.access_token}`
              }
            });
            return next(newReq);
          }),
          catchError((refreshError) => {
            console.error('Token refresh failed:', refreshError);
            // Refresh failed - logout and redirect to login
            authService.logout();
            return throwError(() => refreshError);
          })
        );
      }

      // Handle other errors
      if (error.status === 0) {
        console.error('Network error:', error);
      }

      return throwError(() => error);
    })
  );
};

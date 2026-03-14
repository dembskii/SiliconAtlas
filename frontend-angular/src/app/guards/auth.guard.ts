import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { catchError, of } from 'rxjs';
import { map } from 'rxjs/operators';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Check if token exists
  const token = authService.getToken();
  console.log('Auth Guard: Token exists?', !!token);
  
  if (!token) {
    console.log('Auth Guard: No token, redirecting to login');
    router.navigate(['/login']);
    return false;
  }

  // If token is expired, try to refresh it
  if (authService.isTokenExpired()) {
    console.log('Auth Guard: Token expired, attempting refresh');
    return authService.refreshToken().pipe(
      map(() => {
        console.log('Auth Guard: Token refreshed successfully');
        return true;
      }),
      catchError((error) => {
        console.error('Auth Guard: Token refresh failed', error);
        router.navigate(['/login']);
        return of(false);
      })
    );
  }

  console.log('Auth Guard: Token valid, allowing access');
  return true;
};

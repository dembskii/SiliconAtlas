import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('access_token');

  console.log('Auth Interceptor - Processing request:', {
    url: req.url,
    hasToken: !!token,
    tokenPrefix: token?.substring(0, 30) + '...'
  });

  if (token) {
    const cloned = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    console.log('Auth Interceptor - Added Authorization header');
    return next(cloned);
  }

  console.log('Auth Interceptor - No token, sending without auth');
  return next(req);
};

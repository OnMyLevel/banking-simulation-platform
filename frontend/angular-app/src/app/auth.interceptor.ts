import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthTokenService } from './auth-token.service';

export const AUTHORIZATION_HEADER = 'Authorization';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  if (request.headers.has(AUTHORIZATION_HEADER)) {
    return next(request);
  }

  const token = inject(AuthTokenService).getToken();

  if (!token) {
    return next(request);
  }

  return next(
    request.clone({
      setHeaders: {
        [AUTHORIZATION_HEADER]: `Bearer ${token}`,
      },
    }),
  );
};

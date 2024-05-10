import {
  HTTP_INTERCEPTORS,
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable, catchError, switchMap, throwError } from 'rxjs';
import { LoginService } from './login/login.service';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private loginService: LoginService, private router: Router) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    let authRequest = req;
    //Not setting the token if request is for login/refresh--token not required for it
    if (
      authRequest.url.startsWith('/upload') ||
      authRequest.url.split('8082')[1].startsWith('/api')
    ) {
    } else {
      const token = this.loginService.getToken();
      if (token != null) {
        authRequest = authRequest.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
          },
        });
      }
    }
    return next.handle(authRequest).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 403) {
          const refreshtoken = this.loginService.getRefreshToken();

          if (refreshtoken) {
            return this.handle403Error(authRequest, next, refreshtoken);
          }
        }

        return throwError(error);
      })
    );
  }

  private handle403Error(
    req: HttpRequest<any>,
    next: HttpHandler,
    refreshToken: string
  ): Observable<HttpEvent<any>> {
    return this.loginService.getrefreshTokenApi(refreshToken).pipe(
      switchMap((resp: any) => {
        if (resp.accesstoken) {
          this.loginService.loginUser(resp.accesstoken, resp.refreshtoken);
          const authReq = req.clone({
            setHeaders: { Authorization: `Bearer ${resp.accesstoken}` },
          });
          return next.handle(authReq);
        } else {
          this.loginService.logout();
          this.router.navigate(['login']);
          return throwError('Failed to refresh token');
        }
      }),
      catchError((err) => {
        // Handle refresh token failure, for example by redirecting to login
        this.router.navigate(['login']);
        return throwError(err);
      })
    );
  }
}
export const authInterceptorProviders = [
  {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true,
  },
];

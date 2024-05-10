import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from '../login/login.service';

export const empGuard: CanActivateFn = (route, state) => {
  const loginService = inject(LoginService);
  const router = inject(Router);
  if (
    loginService.isLoggedIn() &&
    loginService.getUserRole() == 'ROLE_EMPLOYEE'
  ) {
    return true;
  } else if (
    loginService.isLoggedIn() &&
    loginService.getUserRole() == 'ROLE_HR'
  ) {
    router.navigate(['/admin/dashboard']);
    return false; // You may want to return false here if the user is an employee
  } else {
    loginService.logout();
    router.navigate(['login']);
    return false;
  }
};

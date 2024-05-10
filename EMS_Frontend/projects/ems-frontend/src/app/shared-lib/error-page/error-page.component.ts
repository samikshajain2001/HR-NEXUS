import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../../services/login/login.service';

@Component({
  selector: 'app-error-page',
  templateUrl: './error-page.component.html',
  styleUrls: ['./error-page.component.css'],
})
export class ErrorPageComponent {
  constructor(private router: Router, private loginService: LoginService) {}

  role = this.loginService.getUserRole();

  isAdminRoute(): boolean {
    // Get the current URL
    const currentUrl = this.router.url;

    // Check if it starts with "/admin/"
    return currentUrl.startsWith('/admin/');
  }
}

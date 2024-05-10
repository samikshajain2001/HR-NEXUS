import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { LoginService } from '../services/login/login.service';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  constructor(
    private loginService: LoginService,
    private router: Router,
    private ngxService: NgxUiLoaderService
  ) {}

  @ViewChild('f')
  loginFormControls!: NgForm;
  formInputData!: Object;

  //variables to show error on frontend
  usernameError: boolean = true;
  passwordError: boolean = true;
  errorMsg: string = '**Invalid Credentials';
  errorhide: boolean = true;
  onSubmit(): void {
    this.ngxService.startLoader('master');
    ({
      form: { value: this.formInputData },
    } = this.loginFormControls);
    //added Validations
    if (this.loginFormControls.value.userName.trim().length == 0) {
      this.usernameError = false;
      this.errorhide = true;
      this.ngxService.stopLoader('master');
    } else if (this.loginFormControls.value.password.trim().length == 0) {
      this.usernameError = true;
      this.passwordError = false;
      this.errorhide = true;
      this.ngxService.stopLoader('master');
    } else {
      this.usernameError = true;
      this.passwordError = true;
      this.errorhide = true;
      //calling service method to perform login --fields are validated
      this.loginService.authenticateUser(this.formInputData).subscribe({
        next: (data: any) => {
          const token = data.accesstoken;
          const refresh = data.refreshtoken;
          if (this.loginService.loginUser(token, refresh)) {
            this.loginService.getCurruser().subscribe({
              next: (user) => {
                this.loginService.setUser(user);
                const role = this.loginService.getUserRole();
                if (role == 'ROLE_HR') {
                  this.router.navigate(['/admin/dashboard']);
                }
                if (role == 'ROLE_EMPLOYEE') {
                  this.router.navigate(['/emp/profile/personalinfo']);
                }
                this.ngxService.stopLoader('master');
              },
              error: (err) => {
                this.ngxService.stopLoader('master');
                this.errorhide = false;
              },
            });
          }
        },
        error: (err) => {
          this.ngxService.stopLoader('master');
          this.errorhide = false;
        },
      });
    }
  }
}

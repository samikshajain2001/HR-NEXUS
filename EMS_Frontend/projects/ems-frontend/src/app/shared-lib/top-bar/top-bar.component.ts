import { Component, ViewChild } from '@angular/core';
import { LoginService } from '../../services/login/login.service';
import { NgToastService } from 'ng-angular-popup';
import { ChangePasswordService } from '../../services/Change_Password/change-password.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-top-bar',
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.css'],
})
export class TopBarComponent {
  user: any;
  date: any;
  @ViewChild('staticBackdropm') modal: any;
  constructor(
    private loginService: LoginService,
    private toast: NgToastService,
    private changepass: ChangePasswordService,
    private router: Router
  ) {
    this.user = this.loginService.getUser();
    this.date = this.getCurrentDate();
  }
  getCurrentDate(): string {
    const currentDate = new Date();
    const options: Intl.DateTimeFormatOptions = {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    };
    return currentDate.toLocaleDateString('en-US', options);
  }

  newpass: { oldpassword: String; password: String; repeatpassword: String } = {
    oldpassword: '',
    password: '',
    repeatpassword: '',
  };

  signInError: String = '';

  responseMessage: String = '';
  responseClass: String = '';

  save(formdata: any) {
    this.newpass.oldpassword = formdata.oldpassword;
    this.newpass.password = formdata.password;
    this.newpass.repeatpassword = formdata.repassword;
    if (this.newpass.repeatpassword != this.newpass.password) {
      this.seToast('Passwords do not match');
      return;
    }

    this.changepass.sendPassword(this.newpass).subscribe({
      next: (response: any) => {
        this.toast.success({
          detail: 'Success',
          summary: `Password Changed Successfully`,
          duration: 2000,
        });

        this.responseMessage = response;
        this.setResponseClass(response);
      },
      error: (error: any) => {
        this.responseMessage = error.error;
        this.setResponseClass(error.error);
        this.seToast(error.error);
      },
    });
  }

  setResponseClass(response: string) {
    if (response === 'Password changed successfully') {
      this.responseClass = 'alert-success';
    } else if (response === 'Password Not matched') {
      this.responseClass = 'alert-warning';
    } else if (
      response === 'Internal Server Error' ||
      response === 'Please Enter Old Password Correctly' ||
      response === 'Invalid Credentials'
    ) {
      this.responseClass = 'alert-danger';
    } else {
      this.responseClass = 'alert-info';
    }
  }
  seToast(message: any) {
    this.toast.error({
      detail: 'Error',
      summary: message,
      duration: 2000,
    });
  }
  togglePassword(fieldId: string, iconId: string) {
    const field = document.getElementById(fieldId) as HTMLInputElement;
    const icon = document.getElementById(iconId);
    if (field.type === 'password') {
      field.type = 'text';
      icon?.classList.remove('bi-eye-slash');
      icon?.classList.add('bi-eye');
    } else {
      field.type = 'password';
      icon?.classList.remove('bi-eye');
      icon?.classList.add('bi-eye-slash');
    }
  }
}

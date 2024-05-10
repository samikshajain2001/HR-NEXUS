import { Component, Input } from '@angular/core';
import { LoginService } from '../../../services/login/login.service';

@Component({
  selector: 'app-personal-info',
  templateUrl: './personal-info.component.html',
  styleUrls: ['./personal-info.component.css'],
})
export class PersonalInfoComponent {
  userData!: any;

  constructor(private loginService: LoginService) {}
  user = {
    firstName: 'John',
    lastName: 'Doe',
    mobileNo: '1234567890',
    personalEmail: 'john@example.com',
    companyEmail: 'john.doe@company.com',
    dob: '1990-01-01',
  };
  ngOnInit() {
    this.loginService.getCurruser().subscribe({
      next: (data: any) => {
        this.userData = data;
      },
      error: (err) => {
      },
    });
  }
}

import { Component } from '@angular/core';
import { LoginService } from '../../../services/login/login.service';

@Component({
  selector: 'app-department-info',
  templateUrl: './department-info.component.html',
  styleUrls: ['./department-info.component.css'],
})
export class DepartmentInfoComponent {
  userData!: any;

  constructor(private loginService: LoginService) {}
  ngOnInit() {
    this.loginService.getCurruser().subscribe({
      next: (data: any) => {
        this.userData = data;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}

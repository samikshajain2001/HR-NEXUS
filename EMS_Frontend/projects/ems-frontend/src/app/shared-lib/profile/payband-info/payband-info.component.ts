import { Component } from '@angular/core';
import { LoginService } from '../../../services/login/login.service';

@Component({
  selector: 'app-payband-info',
  templateUrl: './payband-info.component.html',
  styleUrls: ['./payband-info.component.css'],
})
export class PaybandInfoComponent {
  userData!: any;

  constructor(private loginService: LoginService) {}
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

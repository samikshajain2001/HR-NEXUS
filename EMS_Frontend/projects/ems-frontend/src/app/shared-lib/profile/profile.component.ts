import { Component } from '@angular/core';
import { LoginService } from '../../services/login/login.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent {
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

import { Component, Input } from '@angular/core';
import { LoginService } from '../../../services/login/login.service';

@Component({
  selector: 'app-project-info',
  templateUrl: './project-info.component.html',
  styleUrls: ['./project-info.component.css'],
})
export class ProjectInfoComponent {
  userData!: any;

  constructor(private loginService: LoginService) {}
  ngOnInit() {
    this.loginService.getCurruser().subscribe({
      next: (data: any) => {
        console.log('REACHING HERE');
        this.userData = data;
        console.log(this.userData);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}

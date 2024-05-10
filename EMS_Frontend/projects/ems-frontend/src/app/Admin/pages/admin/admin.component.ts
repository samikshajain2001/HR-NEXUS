import { Component } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css'],
})
export class AdminComponent {
  constructor(private toast: NgToastService) {}
  ngOnInit() {
    this.toast.success({
      detail: 'Success',
      summary: 'Logged in successfully! ',
      duration: 1500,
    });
  }
}

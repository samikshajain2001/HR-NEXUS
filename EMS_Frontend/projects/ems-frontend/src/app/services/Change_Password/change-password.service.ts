import { Injectable } from '@angular/core';
import { LoginService } from '../login/login.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../.././../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ChangePasswordService {
  role: string | null = null;
  constructor(private loginservice: LoginService, private http: HttpClient) {
    this.role = this.loginservice.getUserRole();
  }

  private apiUrl_hr = `${environment.serverUrl}/hr/newpassword`;
  private apiUrl_emp = `${environment.serverUrl}/emp/newpassword`;

  sendPassword(formData: any) {
    // Add role to the formData object

    if (this.role == 'ROLE_HR') {
      console.log(222);

      return this.http.post(this.apiUrl_hr, formData, {
        responseType: 'text',
      });
    } else {
      return this.http.post(this.apiUrl_emp, formData, {
        responseType: 'text',
      });
    }
  }
}

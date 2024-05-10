import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../.././../environments/environment';
import { LoginService } from '../login/login.service';

@Injectable({
  providedIn: 'root',
})
export class ReportdataService {
  constructor(private http: HttpClient, private loginService: LoginService) {}

  generateBasicReport(format: string) {
    return this.http.get(
      `${environment.serverUrl}/hr/genBasicReport?format=${format}&empId=${
        this.loginService.getUser().empID
      }`
    );
  }
  generateCustomReport(
    reportType: string,
    format: string,
    status: string,
    deptName: string,
    gender: string
  ) {
    return this.http.get(
      `${
        environment.serverUrl
      }/hr/generateReport?reportType=${reportType}&format=${format}&empId=${
        this.loginService.getUser().empID
      }&status=${status}&deptName=${deptName}&gender=${gender}`
    );
  }
}

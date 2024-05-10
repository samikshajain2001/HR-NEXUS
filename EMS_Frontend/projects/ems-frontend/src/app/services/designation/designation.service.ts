import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import baseUrl from '../helper';
import { environment } from '../.././../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DesignationService {
  create(designation: any) {
    return this.http.post(
      `${environment.serverUrl}/hr/createDesignation`,
      designation
    );
  }
  constructor(private http: HttpClient) {}

  getDesData() {
    return this.http.get(`${environment.serverUrl}/hr/getAllDesignations`);
  }
}

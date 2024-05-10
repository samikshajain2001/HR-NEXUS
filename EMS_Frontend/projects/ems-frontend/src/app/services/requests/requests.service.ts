import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import baseUrl from '../helper';
import { environment } from '../.././../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class RequestsService {
  constructor(private http: HttpClient) {}

  getRequests(pageNo: number, pageSize: number) {
    return this.http.get(
      `${environment.serverUrl}/hr/getAllRequests?pageNo=${pageNo}&pageSize=${pageSize}`
    );
  }
  requestDecision(status: String, id: number) {
    return this.http.post(
      `${environment.serverUrl}/hr/update-request?status=${status}&id=${id}`,
      null
    );
  }

  getExistRequest() {
    return this.http.get(`${environment.serverUrl}/emp/check-request`);
  }
}

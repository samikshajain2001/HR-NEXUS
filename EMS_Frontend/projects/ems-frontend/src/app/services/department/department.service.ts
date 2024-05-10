import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { DepartmentResponse } from '../../models/DepartmentResponse';
import { Department } from '../../models/Department';
import { Observable } from 'rxjs';
import { environment } from '../.././../environments/environment';

@Injectable({
  providedIn: 'root',
})
//environment.serverUrl
export class DepartmentService {
  http = inject(HttpClient);

  public getDeptData(): Observable<Department[]> {
    return this.http.get<Department[]>(`${environment.serverUrl}/dept/get-all`);
  }
  public getDeptDataPaged(
    pageNumber: number,
    pageSize: number,
    sortBy: string,
    sortDir: string
  ): Observable<DepartmentResponse> {
    return this.http.get<DepartmentResponse>(
      `${environment.serverUrl}/dept/page-all`,
      {
        params: {
          pageNumber: pageNumber,
          pageSize: pageSize,
          sortBy: sortBy,
          sortDir: sortDir,
        },
      }
    );
  }

  public createDepartment(department: Department): Observable<Department> {
    return this.http.post<Department>(
      `${environment.serverUrl}/dept/create`,
      department
    );
  }

  public searchDepartment(
    pageNumber: number,
    pageSize: number,
    name: string,
    sortBy: string,
    sortDir: string
  ): Observable<DepartmentResponse> {
    return this.http.get<DepartmentResponse>(
      `${environment.serverUrl}/dept/search-name`,
      {
        params: {
          pageNumber: pageNumber,
          pageSize: pageSize,
          name: name,
          sortBy: sortBy,
          sortDir: sortDir,
        },
      }
    );
  }
  public deleteDepartmentByid(id: number): Observable<Department> {
    return this.http.delete<Department>(
      `${environment.serverUrl}/dept/del-id/${id}`
    );
  }

  public updateDept(id: number, deptData: Department) {
    return this.http.put<Department>(
      `${environment.serverUrl}/dept/upd-id/${id}`,
      deptData
    );
  }
}

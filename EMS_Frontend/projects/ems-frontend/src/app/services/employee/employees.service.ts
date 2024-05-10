import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Employee } from '../../models/Employee';
import { EmployeeResponse } from '../../models/EmployeeResponse';
import { environment } from '../.././../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class EmployeesService {
  constructor(private http: HttpClient) {}

  public getEmployeesData() {
    return this.http.get<Employee[]>(`${environment.serverUrl}/hr/get-all`);
  }
  public createEmployee(data: Employee) {
    return this.http.post<Employee>(
      `${environment.serverUrl}/hr/createEmployee`,
      data
    );
  }
  public getPageWiseEmployeeData(
    page_number: number,
    page_size: number,
    sortBy?: string,
    sortDir?: string
  ) {
    if (sortBy) {
      return this.http.get<EmployeeResponse>(
        `${environment.serverUrl}/hr/page-all?sortBy=${sortBy}&sortDir=${sortDir}&pageNumber=${page_number}&pageSize=${page_size}`
      );
    } else {
      return this.http.get<EmployeeResponse>(
        `${environment.serverUrl}/hr/page-all`,
        {
          params: {
            pageNumber: page_number,
            pageSize: page_size,
          },
        }
      );
    }
  }
  public filterByDept(dept: String) {
    return this.http.get(
      `${environment.serverUrl}/hr/getEmpByDepartment?deptName=${dept}`
    );
  }
  public filterByProjecttitle(title: String) {
    return this.http.get(
      `${environment.serverUrl}/project/get-all-Employees?projectTitle=${title}`
    );
  }

  public getSearchResults(
    searchQuery: string,
    page_number: number,
    page_size: number,
    sortBy?: string,
    sortDir?: string
  ) {
    let [firstName, lastName] = searchQuery.split(' ');
    if (lastName === undefined) lastName = '';

    return this.http.get<EmployeeResponse>(
      `${environment.serverUrl}/hr/search?firstName=${firstName}&lastName=${lastName}&pageSize=${page_size}&pageNo=${page_number}&sortBy=${sortBy}&sortDir=${sortDir}`,
      {}
    );
  }
  public deleteEmployee(id: number) {
    return this.http.delete<Employee>(
      `${environment.serverUrl}/hr/delete-id/${id}`
    );
  }
  public updateEmp(id: number, empData: any) {
    return this.http.put<Employee>(
      `${environment.serverUrl}/hr/update-id/${id}`,
      empData
    );
  }
}

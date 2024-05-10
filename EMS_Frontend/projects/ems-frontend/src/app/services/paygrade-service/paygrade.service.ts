import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Paygrade } from '../../models/Paygrade';
import { Observable } from 'rxjs';
import { PaygradeResponse } from '../../models/PaygradeResponse';
import { Employee } from '../../models/Employee';
import { environment } from '../.././../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PaygradeService {
  constructor(private http: HttpClient) {}
  public getPaygradeData(): Observable<Paygrade[]> {
    return this.http.get<Paygrade[]>(
      `${environment.serverUrl}/paygrade/get-all`
    );
  }
  public getAllEmpPaygrade(name: string): Observable<Employee[]> {
    return this.http.get<Employee[]>(
      `${environment.serverUrl}/paygrade/get-Employees`,
      {
        params: {
          name: name,
        },
      }
    );
  }
  public createPaygrade(data: any) {
    return this.http.post<Paygrade[]>(
      `${environment.serverUrl}/paygrade/create`,
      data
    );
  }
  public getPageWisePaygradeData(page_number: number, page_size: number) {
    return this.http.get<PaygradeResponse[]>(
      `${environment.serverUrl}/paygrade/page-all`,
      {
        params: {
          pageNumber: page_number,
          pageSize: page_size,
        },
      }
    );
  }
  public searchPaygrade(pageNumber: any, pageSize: any, paygradeName: string) {
    return this.http.get<PaygradeResponse[]>(
      `${environment.serverUrl}/paygrade/search-name`,
      {
        params: {
          pageNumber: pageNumber,
          pageSize: pageSize,
          paygradeName: paygradeName,
        },
      }
    );
  }
  public getSearchResults(
    searchQuery: string,
    page_number: number,
    page_size: number
  ) {
    let paygradeName = searchQuery;

    if (paygradeName === undefined) paygradeName = '';
    return this.http.get<PaygradeResponse>(
      `${environment.serverUrl}/paygrade/search`,
      {
        params: {
          paygradeName: paygradeName,
          pageNo: page_number - 1,
          pageSize: page_size,
        },
      }
    );
  }
  public deletePaygrade(id: number) {
    return this.http.delete(`${environment.serverUrl}/paygrade/del-id/${id}`);
  }
  public updatePaygrade(id: number, paygradeData: any) {
    return this.http.put<Paygrade>(
      `${environment.serverUrl}/paygrade/upd-id/${id}`,
      paygradeData
    );
  }
}

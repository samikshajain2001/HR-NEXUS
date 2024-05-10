import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Paygrade } from '../../models/Paygrade';
import { environment } from '../.././../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PaygradeService {
  constructor(private http: HttpClient) {}
  getPaygradesData() {
    return this.http.get<Paygrade[]>(
      `${environment.serverUrl}/paygrade/get-all`
    );
  }
}

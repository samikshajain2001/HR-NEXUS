import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../.././../environments/environment';
import { map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class ImgService {
  private imgId: any = '';
  constructor(
    private readonly httpClient: HttpClient,
    private http: HttpClient
  ) {}

  upload(file: File): Observable<string> {
    const fordata = new FormData();

    fordata.append('image', file);

    return this.httpClient
      .post('/upload', fordata, { params: { key: environment.apiKey } })
      .pipe(map((response: any) => response['data']['url']));
  }
  deleteImage(url: string) {
    if (url != null) {
      const imgId = url.split('com/')[1].split('/')[0];
      const headers = new HttpHeaders({
        Authorization: `Client-ID ${environment.apiKey}`,
      });

      return this.http.delete(url, { headers });
    } else {
      return null;
    }
  }
}

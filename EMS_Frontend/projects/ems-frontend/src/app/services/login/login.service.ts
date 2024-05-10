import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../.././../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  constructor(private http: HttpClient) {}
  isAuthenticated: boolean = false;

  setUser(user: Object) {
    localStorage.setItem('user', JSON.stringify(user));
  }

  public authenticateUser(loginData: any) {
    return this.http.post(
      `${environment.serverUrl}/api/authenticate`,
      loginData
    );
  }

  public loginUser(token: string, refresh: string) {
    localStorage.setItem('token', token);
    localStorage.setItem('refreshToken', refresh);
    return true;
  }
  public getCurruser() {
    return this.http.get(`${environment.serverUrl}/emp/get-curr-user`);
  }
  public getToken() {
    return localStorage.getItem('token');
  }
  public getUserRole() {
    const user = this.getUser();
    return user.role;
  }
  public getUser() {
    let userStr = localStorage.getItem('user');
    if (userStr != null) {
      return JSON.parse(userStr);
    } else {
      this.logout();
      return null;
    }
  }
  public logout() {
    localStorage.clear();
    return true;
  }
  public isLoggedIn() {
    if (localStorage.getItem('token') != null) {
      return true;
    } else return false;
  }
  public getRefreshToken() {
    return localStorage.getItem('refreshToken');
  }

  public getrefreshTokenApi(refreshToken: any) {
    const user = this.getUser();
    const data = {
      refreshToken: refreshToken,
      username: user.username,
    };
    return this.http.post(`${environment.serverUrl}/api/refresh`, data);
  }

  //for employee profile update request
  public createUpdateRequest(empData: any, username: string, imgUrl: any) {
    empData.profileImgUrl = imgUrl;
    const data = {
      employee: empData,
      requestedBy: username,
    };
    return this.http.put(`${environment.serverUrl}/emp/create-request`, data);
  }
}

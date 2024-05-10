import { ErrorHandler, Injectable, Injector, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { authInterceptorProviders } from './services/auth.interceptor';
import { LoginComponent } from './login/login.component';
import { TopBarComponent } from './shared-lib/top-bar/top-bar.component';
import { SideBarComponent } from './Admin/components/side-bar/side-bar.component';
import { EmpSidebarComponent } from './Employee/components/emp-sidebar/emp-sidebar.component';
import { DashboardComponent } from './Admin/components/dashboard/dashboard.component';
import { EmployeesComponent } from './Admin/components/employees/employees.component';
import { CommonModule } from '@angular/common';

import { DepartmentsComponent } from './Admin/components/departments/departments.component';
import { ProjectsComponent } from './Admin/components/projects/projects.component';
import { PaygradeComponent } from './Admin/components/paygrade/paygrade.component';
import { ReportsComponent } from './Admin/components/reports/reports.component';

import { NgxUiLoaderModule, NgxUiLoaderRouterModule } from 'ngx-ui-loader';
import { MatCardModule } from '@angular/material/card';
import { AdminComponent } from './Admin/pages/admin/admin.component';
import { EmpComponent } from './Employee/pages/emp/emp.component';
import { ErrorPageComponent } from './shared-lib/error-page/error-page.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { EditProfileComponent } from './Employee/components/edit-profile/edit-profile.component';
import { NgToastModule } from 'ng-angular-popup';
import { RequestsComponent } from './Admin/components/requests/requests.component';
import { ProfileComponent } from './shared-lib/profile/profile.component';
import {
  ConfirmBoxConfigModule,
  NgxAwesomePopupModule,
} from '@costlydeveloper/ngx-awesome-popup';
import { NgToastService } from 'ng-angular-popup';
import { PersonalInfoComponent } from './shared-lib/profile/personal-info/personal-info.component';
import { DepartmentInfoComponent } from './shared-lib/profile/department-info/department-info.component';
import { ProjectInfoComponent } from './shared-lib/profile/project-info/project-info.component';
import { PaybandInfoComponent } from './shared-lib/profile/payband-info/payband-info.component';

@Injectable()
export class MyErrorHandler implements ErrorHandler {
  constructor(private injector: Injector) {}

  handleError(error: any): void {
    const toastService = this.injector.get(NgToastService);
    toastService.error({
      detail: 'Fail',
      summary: `Error occurred: ${error}`,
      duration: 2000,
    });
    // You can also perform additional error handling logic here if needed
    console.error('An error occurred:', error);
  }
}
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    TopBarComponent,
    SideBarComponent,
    EmpComponent,
    EmpSidebarComponent,
    DashboardComponent,
    EmpComponent,
    EmployeesComponent,
    ProfileComponent,
    DepartmentsComponent,
    ProjectsComponent,
    ReportsComponent,
    AdminComponent,
    ErrorPageComponent,
    PaygradeComponent,
    EditProfileComponent,
    RequestsComponent,
    PersonalInfoComponent,
    DepartmentInfoComponent,
    ProjectInfoComponent,
    PaybandInfoComponent,
   
  ],
  imports: [
    NgToastModule,
    MatDatepickerModule,
    MatNativeDateModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    MatCardModule,
    ReactiveFormsModule,
    HttpClientModule,
    CommonModule,
    NgxAwesomePopupModule.forRoot({
      colorList: {
        warning: '#9b3855',
      },
    }),
    ConfirmBoxConfigModule.forRoot(),
    NgxUiLoaderModule,
    NgxUiLoaderRouterModule.forRoot({
      loaderId: 'loader2',
    }),
    LoggerModule.forRoot({
      level: NgxLoggerLevel.DEBUG,
      disableConsoleLogging: false,
    }),
  ],
  providers: [
    authInterceptorProviders,
    // { provide: ErrorHandler, useClass: MyErrorHandler },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}

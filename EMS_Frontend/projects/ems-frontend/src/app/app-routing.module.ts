import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './Admin/components/dashboard/dashboard.component';
import { EmployeesComponent } from './Admin/components/employees/employees.component';

import { DepartmentsComponent } from './Admin/components/departments/departments.component';
import { ProjectsComponent } from './Admin/components/projects/projects.component';
import { PaygradeComponent } from './Admin/components/paygrade/paygrade.component';
import { ReportsComponent } from './Admin/components/reports/reports.component';
import { adminGuard } from './services/guards/admin.guard';
import { empGuard } from './services/guards/emp.guard';
import { AdminComponent } from './Admin/pages/admin/admin.component';
import { EmpComponent } from './Employee/pages/emp/emp.component';
import { ErrorPageComponent } from './shared-lib/error-page/error-page.component';
import { EditProfileComponent } from './Employee/components/edit-profile/edit-profile.component';
import { RequestsComponent } from './Admin/components/requests/requests.component';
import { ProfileComponent } from './shared-lib/profile/profile.component';
import { PersonalInfoComponent } from './shared-lib/profile/personal-info/personal-info.component';
import { DepartmentInfoComponent } from './shared-lib/profile/department-info/department-info.component';
import { ProjectInfoComponent } from './shared-lib/profile/project-info/project-info.component';
import { PaybandInfoComponent } from './shared-lib/profile/payband-info/payband-info.component';

// routes need to be eventually seperated into module
const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [adminGuard],
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent,
      },
      {
        path: 'profile',
        component: ProfileComponent,
        children: [
          { path: 'personalinfo', component: PersonalInfoComponent },
          { path: 'departmentinfo', component: DepartmentInfoComponent },
          { path: 'projectinfo', component: ProjectInfoComponent },
          { path: 'paybandinfo', component: PaybandInfoComponent },
        ],
      },
      { path: 'employees', component: EmployeesComponent },
      { path: 'departments', component: DepartmentsComponent },
      { path: 'projects', component: ProjectsComponent },
      { path: 'paygrades', component: PaygradeComponent },
      { path: 'reports', component: ReportsComponent },
      { path: 'requests', component: RequestsComponent },
      { path: 'login', component: LoginComponent },
    ],
  },
  {
    path: 'emp',
    component: EmpComponent,
    canActivate: [empGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      {
        path: 'profile',
        component: ProfileComponent,
        children: [
          { path: 'personalinfo', component: PersonalInfoComponent },
          { path: 'departmentinfo', component: DepartmentInfoComponent },
          { path: 'projectinfo', component: ProjectInfoComponent },
          { path: 'paybandinfo', component: PaybandInfoComponent },
        ],
      },
      { path: 'edit', component: EditProfileComponent },
    ],
  },
  { path: '**', component: ErrorPageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}

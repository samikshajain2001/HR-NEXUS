import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import baseUrl from '../helper';
import { Observable } from 'rxjs';
import { Project } from '../../models/Project';
import { ProjectResponse } from '../../models/ProjectResponse';
import { environment } from '../.././../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ProjectsService {
  http = inject(HttpClient);
  projectData: any;

  public getProjectsData(): Observable<Project[]> {
    return this.http.get<Project[]>(`${environment.serverUrl}/project/get-all`);
  }
  public getProjectDataPaged(
    pageNumber: number,
    pageSize: number
  ): Observable<ProjectResponse> {
    return this.http.get<ProjectResponse>(
      `${environment.serverUrl}/project/page-all`,
      {
        params: {
          pageNumber: pageNumber,
          pageSize: pageSize,
        },
      }
    );
  }
  public createProject(project: Project): Observable<Project> {
    return this.http.post<Project>(
      `${environment.serverUrl}/project/create`,
      project
    );
  }

  public searchProject(
    pageNumber: number,
    pageSize: number,
    projectTitle: string
  ): Observable<ProjectResponse> {
    return this.http.get<ProjectResponse>(
      `${environment.serverUrl}/project/search-name`,
      {
        params: {
          pageNumber: pageNumber,
          pageSize: pageSize,
          projectTitle: projectTitle,
        },
      }
    );
  }
  public deleteProjectByid(id: number): Observable<Project> {
    return this.http.delete<Project>(
      `${environment.serverUrl}/project/del-id/${id}`
    );
  }
  public updateProject(id: number, projectData: Project) {
    return this.http.put<Project>(
      `${environment.serverUrl}/project/upd-id/${id}`,
      projectData
    );
  }
  public filterProjects(
    projectStatus: string,
    deptName: string,
    pageNumber: number,
    pageSize: number
  ): Observable<ProjectResponse> {
    return this.http.get<ProjectResponse>(
      `${environment.serverUrl}/project/byDepartmentAndStatus`,
      {
        params: {
          projectStatus: projectStatus,
          deptName: deptName,
          pageNumber: pageNumber,
          pageSize: pageSize,
        },
      }
    );
  }
}

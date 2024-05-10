import { Component, ViewChild, inject } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ProjectsService } from '../../../services/projects/projects.service';

import { DepartmentService } from '../../../services/department/department.service';
import { NgToastService } from 'ng-angular-popup';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Project } from '../../../models/Project';
import { Department } from '../../../models/Department';
import { EmployeesService } from '../../../services/employee/employees.service';
@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css'],
})
export class ProjectsComponent {
  projectEmployeeCount: any;
  projectData: Project[] = [];
  deptData: Department[] = [];
  projectEmpData: any;
  searchQuery: string = '';
  totalItems: number = 0;
  pageSize: number = 10;
  pages: number[] = [];
  page: number = 1;
  totalPages: number = 0;
  editDept: any = {};

  @ViewChild('f')
  formControls!: NgForm;
  @ViewChild('edit')
  editformControls!: NgForm;
  editData: any;
  formInputData!: any;
  editformInputData!: any;
  selectedProject: Project = {
    projectId: 0,
    projectTitle: '',
    projectStatus: '',
    department: { departmentId: 0, name: '', email: '', mobileNo: '' },
    start_date: '',
    end_date: '',
    projectDesc: '',
  };
  errorMsg: string = '';
  toggleError: boolean = true;
  editErrorMsg: string = '';
  editToggleError: boolean = true;
  editBtnDisabled: boolean = false;
  selectedOption: any = null;
  selectedStatusOption: any;
  selectedDepartmentOption: any = null;
  projectTitleEdit: boolean = true;
  projectStatusEdit: boolean = true;
  projectDescEdit: boolean = true;
  projectEndDateEdit: boolean = true;
  validDate: boolean = true;
  validUpcomingDate: boolean = true;
  pattern: RegExp = /[~`!#$%\^&*+=\-\[\]\\';,/{}|\\":<>\?]/;
  deptName: string = '';

  constructor(
    private projectService: ProjectsService,
    private deptService: DepartmentService,
    private toast: NgToastService,
    private ngxService: NgxUiLoaderService,
    private employeeService: EmployeesService
  ) {}

  ngOnInit(): void {
    this.getProjectData();
    this.requiredData();
  }

  protected calcSlNo(i: any): number {
    return (this.page - 1) * this.pageSize + i;
  }
  protected resetFormOnClose(): void {
    this.editformControls.resetForm();
    this.formControls.resetForm();
    this.deptName = '';
    this.selectedProject = {
      projectId: 0,
      projectTitle: '',
      projectStatus: '',
      department: { departmentId: 0, name: '', email: '', mobileNo: '' },
      start_date: '',
      end_date: '',
      projectDesc: '',
    };
    this.editToggleError = true;
    this.editBtnDisabled = false;
  }
  protected onFilterChange(): void {
    this.projectService
      .filterProjects(
        this.selectedOption,
        this.selectedDepartmentOption,
        this.page - 1,
        this.pageSize
      )
      .subscribe({
        next: (data: any) => {
          this.pageSize = data.pageable.pageSize;
          this.projectData = data.content;
          this.totalItems = data.totalElements;
          this.totalPages = data.totalPages;
          this.setPages(data.totalPages);
        },
        error: (err) => {},
      });
  }

  protected setPages(totalPages: number): void {
    if (totalPages > 0) {
      this.pages = [];
      for (let i = 0; i < totalPages; i++) {
        this.pages.push(i + 1);
      }
    }
  }

  protected getCurrPageData(pageNo: any): void {
    if (this.searchQuery === '') {
      this.page = pageNo;
      this.getProjectData();
    } else {
      this.page = pageNo;
      this.getProjectData();
    }
  }

  protected getProjectData(): void {
    this.projectService
      .getProjectDataPaged(this.page - 1, this.pageSize)
      .subscribe({
        next: (resp: any) => {
          this.pageSize = resp.pageable.pageSize;
          this.projectData = resp.content;
          this.totalItems = resp.totalElements;
          this.totalPages = resp.totalPages;
          this.setPages(resp.totalPages);
        },
        error: (err: any) => {},
      });
  }

  protected nextPage(): void {
    if (this.page < this.totalPages) {
      this.page++;
      this.getProjectData();
    }
  }

  protected previousPage(): void {
    if (this.page > 1) {
      this.page--;
      this.getProjectData();
    }
  }

  protected searchQueryFire(): void {
    this.projectService
      .searchProject(this.page - 1, this.pageSize, this.searchQuery)
      .subscribe({
        next: (resp: any) => {
          this.pageSize = resp.pageable.pageSize;
          this.projectData = resp.content;
          this.totalItems = resp.totalElements;
          this.totalPages = resp.totalPages;
          this.setPages(resp.totalPages);
        },
        error: (err: any) => {},
      });
  }
  protected validateDates(
    startDate: string,
    endDate: string,
    projectStatus: string
  ): boolean {
    if (startDate && endDate && startDate > endDate) {
      this.validDate = false;
      return false;
    }
    if (startDate && endDate) {
      this.validDate = new Date(startDate) < new Date(endDate);
      if (this.validDate) {
        const currentDate: Date = new Date();
        if (projectStatus && projectStatus === 'ONGOING') {
          const dateString: string = currentDate.toDateString();
          this.validDate = new Date(dateString) < new Date(endDate);
        }
        if (projectStatus && projectStatus === 'COMPLETED') {
          const dateString: string = currentDate.toDateString();
          this.validDate = new Date(dateString) > new Date(endDate);
        }
        if (projectStatus && projectStatus === 'UPCOMING') {
          const dateString: string = currentDate.toDateString();
          this.validDate = new Date(dateString) < new Date(endDate);
        }
      }
      return this.validDate;
    }
    this.validDate = true;
    return true;
  }

  protected validUpcoming(
    startDate: string,
    endDate: string,
    projectStatus: string
  ): void {
    if (startDate && endDate && startDate > endDate) {
      this.validUpcomingDate = false;
      return;
    }
    if (startDate) {
      const currentDate: Date = new Date();
      if (projectStatus && projectStatus === 'ONGOING' && startDate) {
        const dateString: string = currentDate.toDateString();
        this.validUpcomingDate = new Date(dateString) > new Date(startDate);
        return;
      }
      if (
        projectStatus &&
        projectStatus === 'COMPLETED' &&
        (startDate || endDate)
      ) {
        const dateString: string = currentDate.toDateString();
        this.validUpcomingDate = new Date() > new Date(startDate);
        return;
      }
      if (projectStatus && projectStatus === 'UPCOMING') {
        const dateString: string = currentDate.toDateString();
        this.validUpcomingDate = new Date() < new Date(startDate);

        return;
      }
    }
    this.validUpcomingDate = true;
    return;
  }

  protected submitForm(): void {
    this.ngxService.startLoader('master');
    ({
      form: { value: this.formInputData },
    } = this.formControls);
    const createbtn = document.getElementById('createbtn');

    if (
      !this.validateDates(
        this.formInputData.start_date,
        this.formInputData.end_date,
        this.formInputData.projectStatus
      )
    ) {
      this.ngxService.stopLoader('master');
      this.toast.error({
        detail: 'Fail',
        summary: `End date must come after start date`,
      });
      return;
    }

    this.projectService.createProject(this.formInputData).subscribe({
      next: (resp: any) => {
        this.getProjectData();
        this.ngxService.stopLoader('master');
        createbtn?.click();
        this.toast.success({
          detail: 'Success',
          summary: `Project created`,
          duration: 2000,
        });
      },
      error: (err: any) => {
        const errMsg = err.error.message.split(',')[0];
        this.ngxService.stopLoader('master');
        this.toast.error({
          detail: 'Fail',
          summary: errMsg,
        });
      },
    });
  }
  protected setDateFormat(selectedDatet: any) {
    const year = selectedDatet[0];
    const month = selectedDatet[1].toString().padStart(2, '0');
    const day = selectedDatet[2].toString().padStart(2, '0');

    const date = `${year}-${month}-${day}`;
    return date;
  }

  protected setItem(projectData: any): void {
    this.requiredData();
    this.selectedProject = { ...projectData };
    if (this.selectedProject.department != null) {
      this.deptName = this.selectedProject?.department.name;
    } else {
      // this.deptName = 'Select';
    }

    if (this.selectedProject.department != null)
      this.editDept = this.selectedProject.department;

    this.selectedProject.start_date = this.setDateFormat(
      this.selectedProject.start_date
    );
    if (this.selectedProject.end_date) {
      this.selectedProject.end_date = this.setDateFormat(
        this.selectedProject.end_date
      );
    }
  }

  protected viewProjectEmployees(projectData: Project): void {
    this.selectedProject = projectData;
    this.employeeService
      .filterByProjecttitle(projectData.projectTitle)
      .subscribe({
        next: (data: any) => {
          this.projectEmpData = data;
          this.projectEmployeeCount = data.length;
        },
        error: () => {},
      });
  }

  protected deleteProject(projectId: any): void {
    const delbtn = document.getElementById('delbtn');
    this.projectService.deleteProjectByid(projectId).subscribe({
      next: (resp: any) => {
        this.getProjectData();
        delbtn?.click();
        this.toast.success({
          detail: 'Success',
          summary: 'Deleted Successfully',
          duration: 2000,
        });
      },
      error: (err: any) => {
        const errorMsg =
          err.error.message.split(',')[0] + ',Unable to Delete Project';
        delbtn?.click();
        this.toast.error({
          detail: 'Fail',
          summary: errorMsg,
        });
      },
    });
  }
  protected handleEditChange(e: Event): void {
    ({
      form: { value: this.editformInputData },
    } = this.editformControls);
    const changedInputField = (e.target as HTMLInputElement).name;
    let { projectTitle, projectStatus, projectDesc, start_date, end_date } =
      this.editformInputData;
    if (changedInputField === 'projectTitle') {
      if (projectTitle.trim().length == 0) {
        this.editToggleError = false;
        this.editBtnDisabled = true;
        this.editErrorMsg = 'Project Title Cannot be empty';
        this.projectTitleEdit = false;
      } else if (
        projectTitle.trim().length > 0 &&
        /[~`!#$@%0-9\^&*+=\-\[\]\\';,/{}|\\":<>\?]/.test(projectTitle.trim())
      ) {
        this.editToggleError = false;
        this.editBtnDisabled = true;
        this.editErrorMsg = 'Project Title Invalid';
        this.projectTitleEdit = false;
      } else {
        this.editToggleError = true;
        this.editBtnDisabled = false;
        this.projectTitleEdit = true;
      }
    }
    if (changedInputField === ' projectStatus') {
      if (projectStatus.toString().trim().length == 0) {
        this.editToggleError = false;
        this.editBtnDisabled = true;
        this.editErrorMsg = 'Project Status Cannot be empty';
        this.projectStatusEdit = false;
      } else {
        this.editToggleError = true;
        this.editBtnDisabled = false;
        this.projectStatusEdit = true;
        this.editErrorMsg = '';
      }
    }
    if (changedInputField === 'projectDesc') {
      if (projectDesc.trim().length == 0) {
        this.editToggleError = false;
        this.editBtnDisabled = true;
        this.editErrorMsg = 'Project Description Cannot be empty';
        this.projectDescEdit = false;
      } else if (
        projectTitle.trim().length > 0 &&
        this.pattern.test(projectTitle.trim())
      ) {
        this.editToggleError = false;
        this.editBtnDisabled = true;
        this.editErrorMsg = 'Project Description Invalid';
        this.projectDescEdit = false;
      } else {
        this.editToggleError = true;
        this.editBtnDisabled = false;
        this.projectDescEdit = true;
      }
    }
    // if (
    //   changedInputField === 'start_date' ||
    //   changedInputField === 'end_date'
    // )
    // {!start_date
    //     ? (start_date = this.selectedProject.start_date)
    //     : (start_date = start_date);

    //   if (!end_date) {
    //   } else {
    //     if (!this.validateDates(start_date, end_date,projectStatus)) {
    //       this.editToggleError = false;
    //       this.editBtnDisabled = true;
    //       this.editErrorMsg = 'End date must come after start date';
    //       this.projectEndDateEdit = false;
    //     } else {
    //       this.editToggleError = true;
    //       this.editBtnDisabled = false;
    //       this.projectEndDateEdit = true;
    //       this.editErrorMsg = '';
    //     }
    //   }
    // }

    if (!this.projectTitleEdit) {
      this.editToggleError = false;
      this.editBtnDisabled = true;
      this.editErrorMsg = 'Invalid Project Title ';
    }
    if (!this.projectStatusEdit) {
      this.editToggleError = true;
      this.editBtnDisabled = false;
      this.editErrorMsg = 'Status Cannot be empty';
    }
    if (!this.projectDescEdit) {
      this.editToggleError = false;
      this.editBtnDisabled = true;
      this.editErrorMsg = ' Invalid Project Description';
    }
    // if (!this.projectEndDateEdit) {
    //   this.editToggleError = false;
    //   this.editBtnDisabled = true;
    //   this.editErrorMsg = 'Invalid End Date';
    // }
  }
  protected editForm(): void {
    ({
      form: { value: this.editformInputData },
    } = this.editformControls);
    if (!this.editToggleError) {
    } else {
      this.editformInputData.projectStatus == ''
        ? (this.editformInputData.projectStatus = null)
        : (this.editformInputData.projectStatus =
            this.editformInputData.projectStatus);
      this.editformInputData.start_date == ''
        ? (this.editformInputData.start_date = null)
        : (this.editformInputData.start_date =
            this.editformInputData.start_date);
      this.editformInputData.end_date == ''
        ? (this.editformInputData.end_date = null)
        : (this.editformInputData.end_date = this.editformInputData.end_date);
      this.editformInputData.department == ''
        ? (this.editformInputData.department = null)
        : (this.editformInputData.department =
            this.editformInputData.department);
      const delbtn = document.getElementById('edit');
      this.projectService
        .updateProject(this.selectedProject.projectId, this.editformInputData)
        .subscribe({
          next: (data: any) => {
            this.getProjectData();
            delbtn?.click();
            this.toast.success({
              detail: 'Success',
              summary: `Project details updated`,
              duration: 2000,
            });
          },
          error: (err: any) => {
            const errMsg = err.error.message.split(',')[0];
            delbtn?.click();
            this.toast.error({
              detail: 'Error',
              summary: errMsg,
              duration: 2000,
            });
          },
        });
    }
  }
  protected requiredData(): void {
    this.deptService.getDeptData().subscribe({
      next: (data) => {
        this.deptData = data;
      },
      error: (err) => {},
    });
  }

  protected clearFilter(): void {
    this.searchQuery = '';
    this.selectedOption = null;
    if (this.selectedDepartmentOption != null) {
      this.onFilterChange();
    } else this.getProjectData();
  }

  protected clearDepartmentFilter(): void {
    this.searchQuery = '';
    this.selectedDepartmentOption = null;
    if (this.selectedOption != null) {
      this.onFilterChange();
    } else this.getProjectData();
  }
}

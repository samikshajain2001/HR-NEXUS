import { Component, ErrorHandler, ViewChild, inject } from '@angular/core';
import { DepartmentService } from '../../../services/department/department.service';
import { NgForm } from '@angular/forms';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { NgToastService } from 'ng-angular-popup';
import { DepartmentResponse } from '../../../models/DepartmentResponse';
import { Department } from '../../../models/Department';
import { HttpErrorResponse } from '@angular/common/http';
import { EmployeesService } from '../../../services/employee/employees.service';
@Component({
  selector: 'app-departments',
  templateUrl: './departments.component.html',
  styleUrls: ['./departments.component.css'],
})
export class DepartmentsComponent {
  deptEmployeeCount: any;
  constructor(
    private deptService: DepartmentService,
    private ngxService: NgxUiLoaderService,
    private toast: NgToastService,
    private employeeService: EmployeesService
  ) {}

  deptData: Department[] = [];
  searchQuery: string = '';
  deptEmployeeData: any;
  totalItems: number = 0;
  pageSize: number = 10;
  pages: number[] = [];
  page: number = 1;
  totalPages: number = 0;
  @ViewChild('f')
  formControls!: NgForm;
  @ViewChild('edit')
  editformControls!: NgForm;
  formInputData!: any;
  editformInputData!: any;
  selectedDept: Department = {
    departmentId: 0,
    name: '',
    email: '',
    mobileNo: '',
  };
  editBtnDisabled: boolean = false;
  invalidNameCharacters: boolean = false;
  invalidEmailCharacters: boolean = false;
  invalidMobile: boolean = false;
  emailInvalid: boolean = false;
  public sortParam: string = 'departmentId desc';
  ngOnInit(): void {
    this.getDepartmentData();
  }

  protected validateInput(value: string, field: string): void {
    if (field === 'name') {
      if (value) {
        const disallowedChars = /[~`!#$%^&@0-9*+=\-[\]\\';,/{}|\\":<>]/;
        this.invalidNameCharacters = disallowedChars.test(value);
        this.editBtnDisabled = this.invalidNameCharacters;
      } else {
        this.editBtnDisabled = true;
      }
    } else if (field === 'email') {
      if (value) {
        const disallowedChars = /[~`!#$%^&*+=\-[\]\\';,/{}|\\":<>]/;
        this.invalidEmailCharacters = disallowedChars.test(value);
        this.emailInvalid = !value.includes('@');
        this.editBtnDisabled = this.invalidEmailCharacters;
      } else {
        this.editBtnDisabled = true;
      }
    } else {
      if (value) {
        if (value.toString().length != 10) {
          this.invalidMobile = true;
        } else {
          this.invalidMobile = false;
        }
        this.editBtnDisabled = this.invalidMobile;
      }
    }
  }
  protected createResetFields(): void {
    this.emailInvalid = false;
    this.invalidNameCharacters = false;
    this.invalidEmailCharacters = false;
    this.invalidMobile = false;
  }

  protected resetFields(): void {
    this.emailInvalid = false;
    this.invalidNameCharacters = false;
    this.invalidEmailCharacters = false;
    this.invalidMobile = false;
    this.editformControls.resetForm();
    this.formControls.resetForm();
    this.editBtnDisabled = false;
    this.selectedDept = {
      departmentId: 0,
      name: '',
      email: '',
      mobileNo: '',
    };
  }
  protected sortEmp(): void {
    if (this.searchQuery != '') {
      this.searchQueryFire();
    } else {
      this.getDepartmentData();
    }
  }
  protected clearFilter(): void {
    this.searchQuery = '';
    this.sortParam = 'departmentId desc';
    this.page = 1;
    this.getDepartmentData();
  }

  protected calcSlNo(i: number): number {
    return (this.page - 1) * this.pageSize + i;
  }

  protected setPages(totalPages: number): void {
    if (totalPages > 0) {
      this.pages = [];
      for (let i = 0; i < totalPages; i++) {
        this.pages.push(i + 1);
      }
    }
  }

  protected getCurrPageData(pageNo: number): void {
    if (this.searchQuery === '') {
      this.page = pageNo;
      this.getDepartmentData();
    } else {
      this.page = pageNo;
      this.getDepartmentData();
    }
  }
  //add model
  protected getDepartmentData(): void {
    const sortBy = this.sortParam.split(' ')[0];
    const sortDir = this.sortParam.split(' ')[1];
    this.deptService
      .getDeptDataPaged(this.page - 1, this.pageSize, sortBy, sortDir)
      .subscribe({
        next: (resp: DepartmentResponse) => {
          this.pageSize = resp.pageable.pageSize;
          this.deptData = resp.content;
          this.totalItems = resp.totalElements;
          this.totalPages = resp.totalPages;
          this.setPages(resp.totalPages);
        },
        error: (err: HttpErrorResponse) => {},
      });
  }

  protected nextPage(): void {
    if (this.page < this.totalPages) {
      this.page++;
      this.getDepartmentData();
    }
  }

  protected previousPage(): void {
    if (this.page > 1) {
      this.page--;
      this.getDepartmentData();
    }
  }
  //add model
  protected searchQueryFire(): void {
    const sortBy = this.sortParam.split(' ')[0];
    const sortDir = this.sortParam.split(' ')[1];
    this.deptService
      .searchDepartment(
        this.page - 1,
        this.pageSize,
        this.searchQuery,
        sortBy,
        sortDir
      )
      .subscribe({
        next: (resp: DepartmentResponse) => {
          this.pageSize = resp.pageable.pageSize;
          this.deptData = resp.content;
          this.totalItems = resp.totalElements;
          this.totalPages = resp.totalPages;
          this.setPages(resp.totalPages);
        },
        error: (err: HttpErrorResponse) => {},
      });
  }

  protected submitForm(): void {
    this.ngxService.startLoader('master');
    ({
      form: { value: this.formInputData },
    } = this.formControls);
    //department field validations
    if (
      this.invalidEmailCharacters ||
      this.invalidMobile ||
      this.invalidNameCharacters
    ) {
    } else {
      const createbtn = document.getElementById('createbtn');
      this.deptService.createDepartment(this.formInputData).subscribe({
        next: (resp: Department) => {
          this.getDepartmentData();
          this.ngxService.stopLoader('master');
          createbtn?.click();
          this.toast.success({
            detail: 'Success',
            summary: `Department successfully Created`,
            duration: 2000,
          });
        },
        error: (err: HttpErrorResponse) => {
          this.ngxService.stopLoader('master');
          const errorMsg = err.error.message.split(',')[0];
          createbtn?.click();
          this.toast.error({
            detail: 'Error',
            summary: errorMsg,
            duration: 2000,
          });
        },
      });
    }
  }

  protected setItem(deptData: Department): void {
    this.selectedDept = deptData;
  }
  protected viewDeptEmployees(deptData: Department): void {
    this.selectedDept = deptData;
    this.employeeService.filterByDept(deptData.name).subscribe({
      next: (data: any) => {
        this.deptEmployeeData = data;
        this.deptEmployeeCount = data.length;
      },
      error: () => {},
    });
  }
  protected deleteDept(departmentId: number): void {
    const delbtn = document.getElementById('delbtn');
    this.deptService.deleteDepartmentByid(departmentId).subscribe({
      next: (resp: Department) => {
        this.getDepartmentData();
        delbtn?.click();
        this.toast.success({
          detail: 'Success',
          summary: `Department Deleted Successfully`,
          duration: 2000,
        });
      },
      error: (err: HttpErrorResponse) => {
        const errorMsg = err.error.message.split(',')[0];
        delbtn?.click();
        this.toast.error({
          detail: 'Error',
          summary: errorMsg,
          duration: 2000,
        });
      },
    });
  }
  //add model
  protected editForm(): void {
    ({
      form: { value: this.editformInputData },
    } = this.editformControls);
    if (
      this.invalidEmailCharacters ||
      this.invalidMobile ||
      this.invalidNameCharacters
    ) {
    } else {
      const editbtn = document.getElementById('editbtn');
      this.deptService
        .updateDept(this.selectedDept.departmentId, this.editformInputData)
        .subscribe({
          next: (resp: Department) => {
            this.getDepartmentData();
            editbtn?.click();
            this.toast.success({
              detail: 'Success',
              summary: `Department details updated`,
              duration: 2000,
            });
          },
          error: (err: HttpErrorResponse) => {
            const errorMsg = err.error.message.split(',')[0];
            editbtn?.click();
            this.toast.error({
              detail: 'Error',
              summary: errorMsg,
              duration: 2000,
            });
          },
        });
    }
  }
}

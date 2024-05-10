import { Component, ViewChild } from '@angular/core';
import { EmployeesService } from '../../../services/employee/employees.service';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { ImgService } from '../../../services/img-upload/img-upload/img.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';

import { ProjectsService } from '../../../services/projects/projects.service';
import { PaygradeService } from '../../../services/paygrades/paygrade.service';
import { DepartmentService } from '../../../services/department/department.service';
import { NgToastService } from 'ng-angular-popup';
import { DesignationService } from '../../../services/designation/designation.service';
import { Employee } from '../../../models/Employee';
import { Department } from '../../../models/Department';
import { EmployeeResponse } from '../../../models/EmployeeResponse';
import { Paygrade } from '../../../models/Paygrade';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginService } from '../../../services/login/login.service';

@Component({
  selector: 'app-employees',
  templateUrl: './employees.component.html',
  styleUrls: ['./employees.component.css'],
})
export class EmployeesComponent {
  @ViewChild('f')
  formControls!: NgForm;
  @ViewChild('df') myForm!: NgForm;
  editData: any = {};
  userData: any = {};
  public page: number = 1;
  public pageSize: number = 10;
  public totalPages: number = 0;
  public totalItems: number = 0;
  public pages: number[] = [];
  public searchQuery: string = '';
  public deptData: Department[] = [];
  public desData: any;
  public projectData: any;
  public paygradeData: Paygrade[] = [];
  public sortParam: any = 'empID desc';
  public deptName: string = '';
  public payName: string = '';
  public projectName: string = '';
  public desgnName: string = '';
  public currId: number = 0;

  public designation: any = {
    title: '',
    description: '',
  };
  formInputData!: any;
  editformInputData!: any;
  employeesData: Employee[] = [];
  toggleError: boolean = true;
  errorMsg: string = '';
  valid: boolean = false;
  firstnameValid: boolean = true;
  lastnameValid: boolean = true;
  emailValid: boolean = true;
  mobileValid: boolean = true;
  dobValid: boolean = true;
  genderValid: boolean = true;
  joiningDatevalid: boolean = true;
  cityValid: boolean = true;
  streetValid: boolean = true;
  roleValid: boolean = true;
  submitBtnDisabled: boolean = true;
  companyemailValid: boolean = true;
  count: number = 0;
  requiredFieldCheck: boolean = true;
  requiredFieldCheck1: boolean = true;
  lastNameCheck: boolean = false;
  firstNameCheck: boolean = false;
  emailCheck: boolean = false;
  mobileCheck: boolean = false;
  dobCheck: boolean = false;
  jdCheck: boolean = false;
  genderCheck: boolean = false;
  roleCheck: boolean = false;
  cemailValid: boolean = true;
  cemailCheck: boolean = false;
  pincodeValid: boolean = true;
  pincodeCheck: boolean = false;
  x: boolean = false;
  constructor(
    private employeeService: EmployeesService,
    private img: ImgService,
    private ngxService: NgxUiLoaderService,
    private deptService: DepartmentService,
    private projectService: ProjectsService,
    private paygradesService: PaygradeService,
    private toast: NgToastService,
    private designationService: DesignationService,
    private loginService: LoginService
  ) {}

  ngOnInit() {
    this.currId = this.loginService.getUser().empID;
    this.getEmployeeData();
  }

  protected calcSlNo(i: number): number {
    return (this.page - 1) * this.pageSize + i;
  }
  // emp create validation
  protected test(value: any, field: string): void {
    switch (field) {
      case 'lastName':
        if (value && !/^[a-zA-Z]+$/.test(value.trim())) {
          this.lastnameValid = false;
          this.lastNameCheck = false;
        } else {
          this.lastnameValid = true;
          this.lastNameCheck = true;
        }
        break;

      case 'firstName':
        if (value && !/^[a-zA-Z]+$/.test(value.trim())) {
          this.firstnameValid = false;
          this.firstNameCheck = false;
        } else {
          this.firstnameValid = true;
          this.firstNameCheck = true;
        }
        break;

      case 'dob':
        const dob = new Date(value);
        const age = Math.floor(
          (Date.now() - dob.getTime()) / (1000 * 60 * 60 * 24 * 365)
        );
        if (!value || age >= 60 || age <= 17) {
          this.dobValid = false;
          this.dobCheck = false;
        } else {
          this.dobValid = true;
          this.dobCheck = true;
        }
        break;

      case 'joiningDate':
        const currentDate = new Date();
        const oneYearAgo = new Date(currentDate);
        oneYearAgo.setFullYear(currentDate.getFullYear() - 10);
        const oneYearLater = new Date(currentDate);
        oneYearLater.setFullYear(currentDate.getFullYear() + 10);
        const joiningDate = new Date(value);
        if (joiningDate >= oneYearAgo && joiningDate <= oneYearLater) {
          this.joiningDatevalid = true;
          this.jdCheck = true;
        } else {
          this.joiningDatevalid = false;
          this.jdCheck = false;
        }
        break;

      case 'email':
        if (
          value &&
          !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value.trim())
        ) {
          this.emailValid = false;
          this.emailCheck = false;
        } else {
          this.emailValid = true;
          this.emailCheck = true;
        }
        break;

      case 'cemail':
        if (
          value &&
          !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value.trim())
        ) {
          this.cemailValid = false;
          this.cemailCheck = false;
        } else {
          this.cemailValid = true;
          this.cemailCheck = true;
        }
        break;
      case 'mobile':
        if (value && value.toString().length != 10) {
          this.mobileValid = false;
          this.mobileCheck = false;
        } else {
          this.mobileValid = true;
          this.mobileCheck = true;
        }
        break;

      case 'gender':
        this.genderValid = true;
        this.genderCheck = true;
        break;

      case 'role':
        this.roleValid = true;
        this.roleCheck = true;
        break;

      case 'city':
        if (value) {
          if (!/^[a-zA-Z\s]+$/.test(value.trim())) {
            this.cityValid = false;
          } else {
            this.cityValid = true;
          }
        } else {
          this.cityValid = true;
        }
        break;

      case 'pincode':
        if (value && value.toString().length != 6) {
          this.pincodeValid = false;
          this.pincodeCheck = false;
        } else {
          this.pincodeValid = true;
          this.pincodeCheck = true;
        }
        break;

      case 'street':
        if (value) {
          if (!/^[a-zA-Z\s]+$/.test(value.trim())) {
            this.streetValid = false;
          } else {
            this.streetValid = true;
          }
        } else {
          this.streetValid = true;
        }
        break;
    }

    if (
      this.firstNameCheck &&
      this.lastNameCheck &&
      this.dobCheck &&
      this.jdCheck &&
      this.emailCheck &&
      this.genderCheck &&
      this.roleCheck &&
      this.cemailValid &&
      this.mobileValid &&
      this.cityValid &&
      this.streetValid &&
      this.pincodeValid
    ) {
      this.requiredFieldCheck = false;
      this.requiredFieldCheck1 = true;
    } else {
      this.requiredFieldCheck = true;
      this.requiredFieldCheck1 = false;
    }

    const a =
      !this.firstnameValid ||
      !this.lastnameValid ||
      !this.emailValid ||
      !this.dobValid ||
      !this.genderValid ||
      !this.joiningDatevalid ||
      !this.roleValid;
    const b =
      this.cemailValid &&
      this.mobileValid &&
      this.cityValid &&
      this.streetValid &&
      this.pincodeValid;
    if (!a && b) {
      this.x = false;
    } else {
      this.x = true;
    }
  }
  protected test2(): void {
    this.submitBtnDisabled = false;
  }

  protected sortEmp(): void {
    if (this.searchQuery != '') {
      this.searchQueryFire();
    } else {
      this.getEmployeeData();
    }
  }

  protected setPage(pageNumber: number): void {
    if (this.searchQuery === '') {
      this.page = pageNumber;
      this.getEmployeeData();
    } else if (this.searchQuery != '') {
      this.page = pageNumber;
      this.searchQueryFire();
    }
  }

  protected clearFilter(): void {
    this.searchQuery = '';
    this.sortParam = 'empID desc';
    this.page = 1;
    this.getEmployeeData();
  }

  protected searchQueryFire(): void {
    const sortBy = this.sortParam.split(' ')[0];
    const sortDir = this.sortParam.split(' ')[1];
    if (this.searchQuery === '') {
      this.getEmployeeData();
    } else {
      this.employeeService
        .getSearchResults(
          this.searchQuery,
          this.page - 1,
          this.pageSize,
          sortBy,
          sortDir
        )
        .subscribe({
          next: (data: any) => {
            this.employeesData = data.content;
            this.totalPages = data.totalPages;
            this.createPagesArray(this.totalPages);
            this.totalItems = data.totalElements;
          },
        });
    }
  }

  protected createPagesArray(totalPages: number): void {
    this.pages = [];
    for (let i = 0; i < totalPages; i++) {
      this.pages.push(i + 1);
    }
  }

  protected onPageSizeChange(): void {
    if (this.searchQuery === '') {
      this.getEmployeeData();
    } else {
      this.searchQueryFire();
    }
  }

  protected getEmployeeData(): void {
    const sortBy = this.sortParam.split(' ')[0];
    const sortDir = this.sortParam.split(' ')[1];
    this.employeeService
      .getPageWiseEmployeeData(this.page - 1, this.pageSize, sortBy, sortDir)
      .subscribe({
        next: (data: EmployeeResponse) => {
          {
            this.employeesData = data.content;
            this.totalPages = data.totalPages;
            this.createPagesArray(this.totalPages);
            this.totalItems = data.totalElements;
          }
        },
        error: (err) => {},
      });
  }

  protected previousPage(): void {
    this.page--;
    if (this.searchQuery === '') {
      this.getEmployeeData();
    } else if (this.searchQuery != '') {
      this.searchQueryFire();
    }
  }

  protected nextPage(): void {
    this.page++;
    if (this.searchQuery === '') {
      this.getEmployeeData();
    } else if (this.searchQuery != '') {
      this.searchQueryFire();
    }
  }

  protected submitForm(): void {
    this.ngxService.startLoader('master');
    ({
      form: { value: this.formInputData },
    } = this.formControls);
    this.formInputData.status = 'ACTIVE';

    const input = document.getElementById('profile-img') as HTMLInputElement;
    let file: File;
    file = input.files![0];
    if (file) {
      this.img.upload(file).subscribe((url) => {
        this.formInputData.profileImgUrl = url;
        this.createEmp();
      });
    } else {
      this.formInputData.profileImgUrl = null;
      this.createEmp();
    }
  }

  private createEmp(): void {
    const createbtn = document.getElementById('createbtn');
    this.employeeService.createEmployee(this.formInputData).subscribe({
      next: (data: Employee) => {
        this.getEmployeeData();
        this.ngxService.stopLoader('master');
        createbtn?.click();
        this.toast.success({
          detail: 'Success',
          summary: `Employee successfully added`,
          duration: 2000,
        });
      },
      error: (err: HttpErrorResponse) => {
        this.ngxService.stopLoader('master');
        createbtn?.click();
        this.toast.error({
          detail: 'Fail',
          summary: err.error.message.split(',')[0],
          duration: 2000,
        });
      },
    });
  }
  protected setDateFormat(selectedDate: string): string {
    const year = selectedDate[0];
    const month = selectedDate[1].toString().padStart(2, '0');
    const day = selectedDate[2].toString().padStart(2, '0');

    const date = `${year}-${month}-${day}`;
    return date;
  }
  protected editEmployee(): void {
    const delbtn = document.getElementById('close-edit-btn');
    if (this.userData.empID === this.loginService.getUser().empID) {
      if (this.editData.role != this.loginService.getUser().role) {
        this.toast.error({
          detail: 'Fail',
          summary: 'Cannot Change Your Own Role',
          duration: 2000,
        });
        return;
      }
      if (this.editData.status != this.loginService.getUser().status) {
        this.toast.error({
          detail: 'Fail',
          summary: 'Cannot Change Your Own Status',
          duration: 2000,
        });
        return;
      }

      this.editData.status = null;
      this.editData.role = null;
    }
    this.employeeService
      .updateEmp(this.userData.empID, this.editData)
      .subscribe({
        next: (data: Employee) => {
          this.getEmployeeData();
          delbtn?.click();
          this.toast.success({
            detail: 'Success',
            summary: `Employee details updated`,
            duration: 2000,
          });
        },
        error: (err: HttpErrorResponse) => {
          this.toast.error({
            detail: 'Fail',
            summary: err.error.message.split(',')[0],
            duration: 2000,
          });
        },
      });
  }

  protected selectUser(employee: Employee): void {
    this.requiredData();
    this.userData = { ...employee };
    if (
      this.userData.role === 'ROLE_HR' &&
      this.loginService.getUser().empID != 100
    ) {
      this.x = true;
    } else {
      this.x = false;
    }
    this.userData.department != null
      ? (this.deptName = this.userData.department.name)
      : (this.deptName = '');
    this.userData.designation != null
      ? (this.desgnName = this.userData.designation.title)
      : (this.desgnName = '');
    this.userData.project != null
      ? (this.projectName = this.userData.project.projectTitle)
      : (this.projectName = '');
    this.userData.paygrade != null
      ? (this.payName = this.userData.paygrade.paygradeName)
      : (this.payName = '');

    this.editData = { ...this.userData };
    this.editData.dob = this.setDateFormat(this.editData.dob);
    this.editData.joiningDate = this.setDateFormat(this.editData.joiningDate);
    this.editData.department = this.deptName;
    this.editData.designation = this.editData.designation?.title;
    this.editData.project = this.editData.project?.projectTitle;
    this.editData.paygrade = this.editData.paygrade?.paygradeName;
  }

  protected deleteUser(empid: number): void {
    const delbtn = document.getElementById('delbtn');
    this.employeeService.deleteEmployee(empid).subscribe({
      next: (data: Employee) => {
        this.getEmployeeData();
        delbtn?.click();
        this.toast.success({
          detail: 'Success',
          summary: `Employee Deleted Successfully`,
          duration: 2000,
        });
      },
      error: (err) => {
        this.toast.error({
          detail: 'Error',
          summary: `Error`,
          duration: 2000,
        });
      },
    });
  }

  protected requiredData(): void {
    this.deptService.getDeptData().subscribe({
      next: (data: Department[]) => {
        this.deptData = data;
      },
      error: (err) => {},
    });
    this.projectService.getProjectsData().subscribe({
      next: (data: Object) => {
        this.projectData = data;
      },
      error: (err) => {},
    });
    this.paygradesService.getPaygradesData().subscribe({
      next: (data: Paygrade[]) => {
        this.paygradeData = data;
      },
      error: (err) => {},
    });
    this.designationService.getDesData().subscribe({
      next: (data) => {
        this.desData = data;
      },
      error: (err) => {},
    });
  }
  protected createDesig() {
    this.designationService.create(this.designation).subscribe({
      next: () => {
        this.designation.title = '';
        this.designation.description = '';
        this.toast.success({
          detail: 'Success',
          summary: `Designation successfully added`,
          duration: 2000,
        });
      },
    });
  }
  revert() {
    this.firstnameValid = true;
    this.lastnameValid = true;
    this.emailValid = true;
    this.mobileValid = true;
    this.dobValid = true;
    this.genderValid = true;
    this.joiningDatevalid = true;
    this.cityValid = true;
    this.streetValid = true;
    this.roleValid = true;
    this.submitBtnDisabled = true;
    this.companyemailValid = true;

    this.requiredFieldCheck = true;
    this.lastNameCheck = false;
    this.firstNameCheck = false;
    this.emailCheck = false;
    this.mobileCheck = false;
    this.dobCheck = false;
    this.jdCheck = false;
    this.genderCheck = false;
    this.roleCheck = false;
    this.cemailValid = true;
    this.cemailCheck = false;
    this.pincodeValid = true;
    this.pincodeCheck = false;
    this.requiredFieldCheck1 = true;
    this.x = false;
  }
  reset() {
    this.userData = {};
    this.deptName = '';
    this.projectName = '';
    this.payName = '';
    this.desgnName = '';
  }
  fileSizeError: boolean = false;

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    const fileSizeInMB: number = file.size / (1024 * 1024);
    console.log(fileSizeInMB);
    if (fileSizeInMB > 1) {
      this.fileSizeError = true;
      event.target.value = '';
      console.log('File size greater than 1MB');
    } else {
      this.fileSizeError = false;
      // Proceed with other operations, if any
    }
  }
}

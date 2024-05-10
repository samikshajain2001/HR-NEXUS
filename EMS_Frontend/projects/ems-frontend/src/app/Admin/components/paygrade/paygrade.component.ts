import { Component, ViewChild } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { PaygradeService } from '../../../services/paygrade-service/paygrade.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { NgToastService } from 'ng-angular-popup';
import { Paygrade } from '../../../models/Paygrade';
import { Employee } from '../../../models/Employee';

@Component({
  selector: 'app-paygrade',
  templateUrl: 'paygrade.component.html',
  styleUrls: ['./paygrade.component.css'],
})
export class PaygradeComponent {
  public page: number = 1;
  public pageSize: number = 10;
  public totalPages: number = 0;
  public totalItems: number = 0;
  public pages: number[] = [];
  errorPaygradeMessage: string = '';
  editformInputData!: any;
  public searchQuery: string = '';
  @ViewChild('f')
  formControls!: NgForm;
  formInputData!: any;
  paygradeData: any;
  userData: any = {};
  @ViewChild('edit')
  editformControls!: NgForm;
  selectedPaygrade: any = {};
  submitbtn: boolean = true;
  editBtnDisabled: boolean = false;
  empCount: number = 0;
  currentPaygrade: Paygrade = {
    id: 0,
    paygradeName: '',
    minPay: 0,
    hra: 0,
    pfs: 0,
    da: 0,
    maxPay: 0,
  };
  allEmps: Employee[] = [];

  constructor(
    private paygradeService: PaygradeService,
    private ngxService: NgxUiLoaderService,
    private http: HttpClient,
    private toast: NgToastService
  ) {}

  ngOnInit() {
    this.getpaygradeData();
  }

  protected setPage(pageNumber: any): void {
    if (this.searchQuery === '') {
      this.page = pageNumber;
      this.getpaygradeData();
    } else {
      this.page = pageNumber;
      this.getSearchPaygrade();
    }
  }

  protected calcSlNo(i: any): number {
    return (this.page - 1) * this.pageSize + i;
  }

  public getSearchPaygrade() {
    this.paygradeService
      .getSearchResults(this.searchQuery, this.page, this.pageSize)
      .subscribe({
        next: (data: any) => {
          this.paygradeData = data.content;
          this.totalPages = data.totalPages;
          this.createPagesArray(this.totalPages);
          this.totalItems = data.totalElements;
        },
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

  protected searchQueryFire(): void {
    this.paygradeService
      .searchPaygrade(this.page - 1, this.pageSize, this.searchQuery)
      .subscribe({
        next: (resp: any) => {
          this.pageSize = resp.pageable.pageSize;
          this.paygradeData = resp.content;
          this.totalItems = resp.totalElements;
          this.totalPages = resp.totalPages;
          this.setPages(resp.totalPages);
        },
        error: (err: any) => {},
      });
  }
  protected createPagesArray(totalPages: number): void {
    this.pages = [];
    for (let i = 0; i < totalPages; i++) {
      this.pages.push(i + 1);
    }
  }

  protected onPageSizeChange(): void {
    if (this.searchQuery === '') {
      this.getpaygradeData();
    } else {
      this.getSearchPaygrade();
    }
  }

  protected getpaygradeData(): void {
    this.paygradeService
      .getPageWisePaygradeData(this.page - 1, this.pageSize)
      .subscribe({
        next: (data: any) => {
          {
            this.paygradeData = data.content;
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
      this.getpaygradeData();
    } else {
      this.getSearchPaygrade();
    }
  }

  protected nextPage(): void {
    this.page++;
    if (this.searchQuery === '') {
      this.getpaygradeData();
    } else {
      this.getSearchPaygrade();
    }
  }

  protected submitForm(): void {
    this.ngxService.startLoader('master');
    ({
      form: { value: this.formInputData },
    } = this.formControls);
    const createbtn = document.getElementById('createbtn');
    const { paygradeName, minPay, hra, pfs, da, maxPay } = this.formInputData;
    if (minPay < 0 || hra < 0 || pfs < 0 || da < 0 || maxPay < 0) {
      createbtn?.click();
      this.ngxService.stopLoader('master');
      this.toast.error({
        detail: 'Fail',
        summary: 'Fields cannot be negative',
        duration: 2000,
      });
    } else if (minPay > maxPay) {
      this.ngxService.stopLoader('master');
      this.toast.error({
        detail: 'Fail',
        summary: 'Minimum Pay cannot be less than Maximum Pay',
        duration: 2000,
      });
    } else {
      this.paygradeService.createPaygrade(this.formInputData).subscribe({
        next: (data: any) => {
          this.getpaygradeData();
          this.ngxService.stopLoader('master');
          createbtn?.click();
          this.toast.success({
            detail: 'Success',
            summary: `Paygrade created`,
            duration: 2000,
          });
        },
        error: (err: HttpErrorResponse) => {
          const errorMsg = err.error.message.split(',')[0];
          createbtn?.click();
          this.ngxService.stopLoader('master');
          this.toast.error({
            detail: 'Fail',
            summary: errorMsg,
            duration: 2000,
          });
        },
      });
    }
  }

  protected selectPaygrade(paygrade: Object) {
    this.editBtnDisabled = false;
    this.selectedPaygrade = paygrade;
  }

  protected resetDetails(): void {
    this.selectedPaygrade = {};
  }

  protected requiredData(): void {
    this.paygradeService.getPaygradeData().subscribe({
      next: (data) => {
        this.paygradeData = data;
      },
      error: (err) => {},
    });
  }

  protected deletePaygrade(id: number): void {
    const delbtn = document.getElementById('delbtn');
    this.paygradeService.deletePaygrade(id).subscribe({
      next: (data) => {
        this.paygradeData = this.getpaygradeData();
        delbtn?.click();
        this.toast.success({
          detail: 'Paygrade deleted! Success',
          summary: `Paygrade deleted`,
          duration: 2000,
        });
      },
      error: (err: HttpErrorResponse) => {
        const msg = err.error.message.split(',')[0];
        delbtn?.click();
        this.toast.error({
          detail: 'Fail',
          summary: msg,
          duration: 2000,
        });
      },
    });
  }

  viewPaygrade() {}
  protected editForm(): void {
    ({
      form: { value: this.editformInputData },
    } = this.editformControls);
    
    let { paygradeName, minPay, hra, pfs, da, maxPay } = this.editformInputData;
    //handling null case for minPay and maxPay
    !minPay ? (minPay = this.selectedPaygrade.minPay) : (minPay = minPay);
    !maxPay ? (maxPay = this.selectedPaygrade.maxPay) : (maxPay = maxPay);
    
    const editbtn = document.getElementById('editbtn');
    if (
      (minPay && minPay < 0) ||
      (hra && hra < 0) ||
      (pfs && pfs < 0) ||
      (da && da < 0) ||
      (maxPay && maxPay < 0)
    ) {
      this.ngxService.stopLoader('master');
      editbtn?.click();
      this.toast.error({
        detail: 'Fail',
        summary: 'Fields cannot be negative',
        duration: 2000,
      });
    } else if (minPay > maxPay) {
      this.ngxService.stopLoader('master');
      editbtn?.click();
      this.toast.error({
        detail: 'Fail',
        summary: 'Minimum Pay cannot be Greater than Maximum Pay',
        duration: 2000,
      });
    } else {
      this.paygradeService
        .updatePaygrade(this.selectedPaygrade.id, this.editformInputData)
        .subscribe({
          next: (data: any) => {
            this.getpaygradeData();
            editbtn?.click();
            this.toast.success({
              detail: 'Success',
              summary: `Paygrade details updated`,
              duration: 2000,
            });
          },
          error: (err: any) => {
            const errorMsg = err.error.message.split(',')[0];
            editbtn?.click();
            this.toast.error({
              detail: 'Fail',
              summary: errorMsg,
              duration: 2000,
            });
          },
        });
    }
  }

  protected showAllEmployees(paygrade: Paygrade): void {
    this.currentPaygrade = paygrade;
    this.paygradeService.getAllEmpPaygrade(paygrade.paygradeName).subscribe({
      next: (resp: Employee[]) => {
        this.allEmps = resp;
        this.empCount = this.allEmps.length;
      },
      error: (err: HttpErrorResponse) => {},
    });
  }
}

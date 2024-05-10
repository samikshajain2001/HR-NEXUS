import { Component } from '@angular/core';
import { ReportdataService } from '../../../services/report/reportdata.service';
import { NgToastService } from 'ng-angular-popup';
import { DepartmentService } from '../../../services/department/department.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css'],
})
export class ReportsComponent {
  data: any;
  selectedOption: any = '';
  deptData: any = '';
  filter: any = {
    status: '',
    deptName: '',
    gender: '',
  };
  projectFilter: any = {
    status: '',
    deptName: '',
  };
  format: any = 'pdf';

  constructor(
    private reportService: ReportdataService,
    private toast: NgToastService,
    private deptService: DepartmentService,
    private ngxService: NgxUiLoaderService
  ) {}

  ngOnInit() {
    this.deptService.getDeptData().subscribe({
      next: (data) => {
        this.deptData = data;
      },
      error: (err) => {
      },
    });
  }

  genReport(): void {
    this.ngxService.startLoader('master');
    this.reportService
      .generateCustomReport(
        this.selectedOption,
        this.format,
        this.filter.status,
        this.filter.deptName,
        this.filter.gender
      )
      .subscribe({
        next: (data: any) => {
          this.ngxService.stopLoader('master');
          this.toast.success({
            detail: 'Report Generated',
            summary: data.message,
            duration: 3000,
          });
          this.filter.status = '';
          this.filter.deptName = '';
          this.filter.gender = '';
        },
        error: (err) => {
          this.ngxService.stopLoader('master');
        },
      });
  }
}

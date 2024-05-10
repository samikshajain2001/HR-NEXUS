import { Component } from '@angular/core';
import { DashboardService } from '../../../services/dashboard/dashboard.service';
import { LoginService } from '../../../services/login/login.service';
import {
  Chart,
  registerables,
} from '../../../../../../../node_modules/chart.js';
Chart.register(...registerables);
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent {
  selected: Date = new Date();
  totalEmployees: number = 0;
  totalDepts: number = 0;
  totalProjects: number = 0;
  genderLabels: string[] = [];
  departmentLabels: string[] = [];
  genderValues: string[] = [];
  departmentValues: string[] = [];
  firstName: string = '';
  selectedDate = new Date();
  yearLabels: string[] = [];
  yearValues: string[] = [];
  desigLabels: string[] = [];
  desigValues: string[] = [];

  constructor(
    private dashboardService: DashboardService,
    private loginService: LoginService
  ) {}
  ngOnInit() {
    this.loadData();
    this.firstName = this.loginService.getUser().firstName;
  }

  private renderGenderChart(): void {
    const colors = ['rgb(54, 162, 235)', 'rgb(255, 99, 132)'];
    new Chart('genderChart', {
      type: 'doughnut',
      data: {
        labels: this.genderLabels,
        datasets: [
          {
            label: 'Count',
            data: this.genderValues,
            borderWidth: 1,
            backgroundColor: colors,
          },
        ],
      },
      options: {
        responsive: false,
        plugins: {
          title: {
            display: true,
            text: 'Employee Composition',
            align: 'end',
            position: 'bottom',
            font: {
              size: 18,
            },
          },
        },
      },
    });
  }

  private renderLineChart(): void {
    const labels = this.yearLabels;
    new Chart('lineChart', {
      type: 'line',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Employee Hiring Per Year',
            data: this.yearValues,
            fill: false,
            borderColor: 'rgb(75, 192, 192)',
            tension: 0.1,
          },
        ],
      },
    });
  }

  private renderDeptChart(): void {
    const colors = [
      'rgb(255, 99, 132)',
      'rgb(75, 192, 192)',
      'rgb(255, 205, 86)',
      'rgb(201, 203, 207)',
      'rgb(54, 162, 235)',
      'rgb(238, 130, 238)',
      'rgb(0, 255, 127)',
      'rgb(255, 140, 0)',
      'rgb(218, 112, 214)',
      'rgb(65, 105, 225)',
    ];
    new Chart('deptChart', {
      type: 'bar',
      data: {
        labels: this.departmentLabels,
        datasets: [
          {
            label: 'Count',
            data: this.departmentValues,
            borderWidth: 1,

            backgroundColor: colors,
          },
        ],
      },
      options: {
        plugins: {
          title: {
            display: true,
            text: 'Department Distribution',
            align: 'center',
            position: 'bottom',
            font: {
              size: 18,
            },
          },
        },
      },
    });
  }

  private renderDesigChart(): void {
    const colors = [
      'rgb(255, 99, 132)',
      'rgb(75, 192, 192)',
      'rgb(255, 205, 86)',
      'rgb(201, 203, 207)',
      'rgb(54, 162, 235)',
      'rgb(238, 130, 238)',
      'rgb(0, 255, 127)',
      'rgb(255, 140, 0)',
      'rgb(218, 112, 214)',
      'rgb(65, 105, 225)',
    ];
    new Chart('desigChart', {
      type: 'bar',
      data: {
        labels: this.desigLabels,
        datasets: [
          {
            label: 'Count',
            data: this.desigValues,
            borderWidth: 1,
            backgroundColor: colors,
          },
        ],
      },
      options: {
        plugins: {
          title: {
            display: true,
            text: 'Job Title Wise Count',
            align: 'center',
            position: 'bottom',
            font: {
              size: 18,
            },
          },
        },
      },
    });
  }

  private loadData(): void {
    this.dashboardService.getAdminDashData().subscribe({
      next: (data: any) => {
        console.log(data);
        this.totalEmployees = data.totalNoEmployee;
        this.totalDepts = data.totalNoDepartment;
        this.totalProjects = data.totalNoProject;
        this.genderLabels = Object.keys(data.empGender);
        this.genderValues = this.departmentLabels = Object.values(
          data.empGender
        );
        this.departmentLabels = Object.keys(data.empDepartment);
        this.departmentValues = Object.values(data.empDepartment);
        this.yearLabels = Object.keys(data.yearwise);
        this.yearValues = Object.values(data.yearwise);
        this.desigLabels = Object.keys(data.empDesig);
        this.desigValues = Object.values(data.empDesig);
        this.renderGenderChart();
        this.renderDeptChart();
        this.renderLineChart();
        this.renderDesigChart();
      },
    });
  }
}

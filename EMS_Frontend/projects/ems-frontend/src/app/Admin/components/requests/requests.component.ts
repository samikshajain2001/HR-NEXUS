import { Component } from '@angular/core';
import { RequestsService } from '../../../services/requests/requests.service';
import { NgToastService } from 'ng-angular-popup';
import { ImgService } from '../../../services/img-upload/img-upload/img.service';

@Component({
  selector: 'app-requests',
  templateUrl: './requests.component.html',
  styleUrls: ['./requests.component.css'],
})
export class RequestsComponent {
  requestsData: any = [];
  totalItems: number = 0;
  pageSize: number = 10;
  pages: number[] = [];
  viewCard: boolean = false;
  page: number = 1;
  userData: any = {};
  totalPages: number = 0;
  constructor(
    private requestService: RequestsService,
    private toast: NgToastService,
    private img: ImgService
  ) {}
  calcSlNo(i: any) {
    return (this.page - 1) * this.pageSize + i;
  }
  ngOnInit() {
    this.loadRequests();
  }
  createPagesArray(totalPages: number) {
    this.pages = [];
    for (let i = 0; i < totalPages; i++) {
      this.pages.push(i + 1);
    }
  }
  loadRequests() {
    this.requestService.getRequests(this.page - 1, this.pageSize).subscribe({
      next: (data: any) => {
        for (let cd of data.content) {
          // Get various components of the date
          const date = new Date(cd.createdAt);
          const year = date.getFullYear();
          const month = date.getMonth() + 1; // Months are 0-based, so add 1
          const day = date.getDate();
          const hours = date.getHours();
          const minutes = date.getMinutes();
          const seconds = date.getSeconds();

          // Format the date as a string
          const formattedTime = `${hours.toString().padStart(2, '0')}:${minutes
            .toString()
            .padStart(2, '0')}`;
          const formattedDate = `${day.toString().padStart(2, '0')}-${month
            .toString()
            .padStart(2, '0')}-${year}`;
          cd.createdAt = formattedTime + ' ' + formattedDate;
        }
        this.requestsData = data.content;
        this.totalPages = data.totalPages;
        this.createPagesArray(this.totalPages);
        this.totalItems = data.totalElements;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
  viewRequest(item: any) {
    this.viewCard = true;
    console.log(item);
    this.userData = item;
  }
  requestDecision(status: string) {
    // if (status === 'REJECTED') {
    //   this.img.deleteImage(this.userData.profileImageUrl);
    // }
    this.requestService.requestDecision(status, this.userData.id).subscribe({
      next: (data) => {
        this.viewCard = false;
        this.loadRequests();
        this.toast.success({
          detail: 'Success',
          summary: `Request has been ${status}`,
          duration: 2000,
        });
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
  nextPage() {
    this.page++;
    this.loadRequests();
  }
  setPage(p: any) {
    this.page = p;
    this.loadRequests();
  }
  previousPage() {
    this.page--;
    this.loadRequests();
  }
}

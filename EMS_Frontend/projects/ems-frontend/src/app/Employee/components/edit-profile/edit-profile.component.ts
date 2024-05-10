import { Component, ViewChild } from '@angular/core';
import { LoginService } from '../../../services/login/login.service';
import { NgForm } from '@angular/forms';
import { NgToastService } from 'ng-angular-popup';
import { ImgService } from '../../../services/img-upload/img-upload/img.service';
import { RequestsService } from '../../../services/requests/requests.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css'],
})
export class EditProfileComponent {
  userData: any = {};
  @ViewChild('f')
  formControls!: NgForm;
  formUpdateData!: any;
  editErrorMsg: string = '';
  editToggleError: boolean = true;
  editBtnDisabled: boolean = false;
  patternPin: RegExp = /[~`!#$%\^&*+=\-\[\]\\';,/{}|\\":<>\?]/;
  pattern: RegExp = /[~`!#$%\^&*+=\-[\]\\';,/{}|\\":<>?\s\d]/;
  // pattern: RegExp =/^[a-zA-Z\s]+$/;
  fNameValid: boolean = true;
  lNameValid: boolean = true;
  pEmailValid: boolean = true;
  mobileValid: boolean = true;
  streetValid: boolean = true;
  cityValid: boolean = true;
  pincodeValid: boolean = true;
  dobValid: boolean = true;
  updateBtnDisabled: boolean = false;
  toggleAlert: boolean = true;
  imageUrl: any = null;

  constructor(
    private loginService: LoginService,
    private toast: NgToastService,
    private img: ImgService,
    private reqService: RequestsService,
    private ngxService: NgxUiLoaderService
  ) {}

  ngOnInit() {
    this.loginService.getCurruser().subscribe({
      next: (data) => {
        this.userData = data;
        this.userData.joiningDate = this.setDateFormat(
          this.userData.joiningDate
        );
        this.userData.dob = this.setDateFormat(this.userData.dob);
      },
    });
    this.reqService.getExistRequest().subscribe({
      next: (resp: any) => {
        if (resp != null) {
          this.toggleAlert = false;
          this.updateBtnDisabled = true;
        } else {
          this.toggleAlert = true;
          this.updateBtnDisabled = false;
        }
      },
      error: (err: any) => {},
    });
  }

  setDateFormat(selectedDatet: any) {
    const year = selectedDatet[0];
    const month = selectedDatet[1].toString().padStart(2, '0');
    const day = selectedDatet[2].toString().padStart(2, '0');

    const date = `${year}-${month}-${day}`;
    return date;
  }
  handleEditChange(e: Event) {
    ({
      form: { value: this.formUpdateData },
    } = this.formControls);
    const changedInputField = (e.target as HTMLInputElement).name;
    let {
      firstName,
      lastName,
      personalEmail,
      mobileNo,
      street,
      city,
      pincode,
      state,
      dob,
    } = this.formUpdateData;
    if (changedInputField == 'firstName') {
      if (firstName.trim().length == 0) {
        this.editToggleError = false;
        this.editErrorMsg = 'FirstName Cannot be Empty';
        this.editBtnDisabled = true;
        this.fNameValid = false;
      } else if (
        (firstName.trim().length > 0 && this.pattern.test(firstName.trim())) ||
        firstName.trim().includes('@') ||
        firstName === ''
      ) {
        this.editToggleError = false;
        this.editErrorMsg = 'FirstName Not Valid';
        this.editBtnDisabled = true;
        this.fNameValid = false;
      } else {
        this.editToggleError = true;
        this.editBtnDisabled = false;
        this.fNameValid = true;
      }
    }
    if (changedInputField == 'lastName') {
      if (lastName.trim().length == 0) {
        this.editToggleError = false;
        this.editErrorMsg = 'Last Name Cannot be Empty';
        this.editBtnDisabled = true;
        this.lNameValid = false;
      } else if (
        (lastName.trim().length > 0 && this.pattern.test(lastName.trim())) ||
        lastName.trim().includes('@')
      ) {
        this.editToggleError = false;
        this.editErrorMsg = 'Last Name Not Valid';
        this.editBtnDisabled = true;
        this.lNameValid = false;
      } else {
        this.editToggleError = true;
        this.editBtnDisabled = false;
        this.lNameValid = true;
      }
    }
    if (changedInputField == 'personalEmail') {
      if (personalEmail.trim().length == 0) {
        this.editToggleError = false;
        this.editErrorMsg = 'Personal Email Cannot be Empty';
        this.editBtnDisabled = true;
        this.pEmailValid = false;
      } else if (
        personalEmail.trim().length > 0 &&
        (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(
          personalEmail.trim()
        ) ||
          !personalEmail.includes('@'))
      ) {
        this.editToggleError = false;
        this.editErrorMsg = 'Personal Email Not Valid';
        this.editBtnDisabled = true;
        this.pEmailValid = false;
      } else {
        this.editToggleError = true;
        this.editBtnDisabled = false;
        this.pEmailValid = true;
      }
    }

    if (changedInputField == 'mobileNo') {
      if (!mobileNo) {
        this.editToggleError = false;
        this.editErrorMsg = 'Mobile Cannot be Empty';
        this.editBtnDisabled = true;
        this.mobileValid = false;
      } else if (mobileNo.toString().length != 10) {
        this.editToggleError = false;
        this.editErrorMsg = 'Mobile Number Invalid';
        this.editBtnDisabled = true;
        this.mobileValid = false;
      } else {
        this.editToggleError = true;
        this.editBtnDisabled = false;
        this.mobileValid = true;
      }
    }
    // if (changedInputField == 'street') {
    //   if (street.trim().length == 0) {
    //     this.editToggleError = false;
    //     this.editErrorMsg = 'Street cannot be Empty';
    //     this.editBtnDisabled = true;
    //     this.streetValid = false;
    //   } else if (
    //     (street.trim().length > 0 && this.pattern.test(street.trim())) ||
    //     street.trim().includes('@')
    //   ) {
    //     this.editToggleError = false;
    //     this.editErrorMsg = 'Street Invalid';
    //     this.editBtnDisabled = true;
    //     this.streetValid = false;
    //   } else {
    //     this.editToggleError = true;
    //     this.editBtnDisabled = false;
    //     this.streetValid = true;
    //   }
    // }
    if (changedInputField == 'city') {
      if (city.trim().length == 0) {
        this.editToggleError = false;
        this.editErrorMsg = 'city cannot be Empty';
        this.editBtnDisabled = true;
        this.cityValid = false;
      } else if (city.trim().length > 0 && this.pattern.test(city.trim())) {
        this.editToggleError = false;
        this.editErrorMsg = 'city Invalid';
        this.editBtnDisabled = true;
        this.cityValid = false;
      } else {
        this.editToggleError = true;
        this.editBtnDisabled = false;
        this.cityValid = true;
      }
    }
    if (changedInputField == 'pincode') {
      if (!pincode) {
        this.editToggleError = false;
        this.editErrorMsg = 'pincode cannot be Empty';
        this.editBtnDisabled = true;
        this.pincodeValid = false;
      } else if (
        (pincode.toString().trim().length > 0 &&
          this.patternPin.test(pincode.toString().trim())) ||
        pincode.toString().trim().length < 4
      ) {
        this.editToggleError = false;
        this.editErrorMsg = 'pincode Invalid';
        this.editBtnDisabled = true;
        this.pincodeValid = false;
      } else {
        this.editToggleError = true;
        this.editBtnDisabled = false;
        this.pincodeValid = true;
      }
    }
    if (changedInputField == 'dob') {
      var today = new Date();
      var dobDate = new Date(dob);
      var age = today.getFullYear() - dobDate.getFullYear();
      var monthDiff = today.getMonth() - dobDate.getMonth();
      if (
        monthDiff < 0 ||
        (monthDiff === 0 && today.getDate() < dobDate.getDate())
      ) {
        age--;
      }
      if (age < 20 || age > 60) {
        this.editToggleError = false;
        this.editErrorMsg = 'Age Should not be less than 20 or Greater than 60';
        this.editBtnDisabled = true;
        this.dobValid = false;
      } else {
        this.editToggleError = true;
        this.editBtnDisabled = false;
        this.dobValid = true;
      }
    }
    if (!this.pEmailValid) {
      this.editToggleError = false;
      this.editBtnDisabled = true;
      this.editErrorMsg = 'Personal Email Invalid';
    }
    if (!this.cityValid) {
      this.editToggleError = false;
      this.editBtnDisabled = true;
      this.editErrorMsg = 'City Invalid';
    }
    if (!this.dobValid) {
      this.editToggleError = false;
      this.editBtnDisabled = true;
      this.editErrorMsg = 'DOB Invalid';
    }
    if (!this.fNameValid) {
      this.editToggleError = false;
      this.editBtnDisabled = true;
      this.editErrorMsg = 'First Name Invalid';
    }
    if (!this.lNameValid) {
      this.editToggleError = false;
      this.editBtnDisabled = true;
      this.editErrorMsg = 'Last Name Invalid';
    }
    if (!this.mobileValid) {
      this.editToggleError = false;
      this.editBtnDisabled = true;
      this.editErrorMsg = 'Mobile Number Invalid';
    }
    if (!this.streetValid) {
      this.editToggleError = false;
      this.editBtnDisabled = true;
      this.editErrorMsg = 'Street Invalid';
    }
    if (!this.cityValid) {
      this.editToggleError = false;
      this.editBtnDisabled = true;
      this.editErrorMsg = 'City Invalid';
    }
    if (!this.pincodeValid) {
      this.editToggleError = false;
      this.editBtnDisabled = true;
      this.editErrorMsg = 'Pincode Invalid';
    }
  }
  SaveFileData() {
    ({
      form: { value: this.formUpdateData },
    } = this.formControls);
    this.formUpdateData.dob == ''
      ? (this.formUpdateData.dob = null)
      : (this.formUpdateData.dob = this.formUpdateData.dob);
    this.formUpdateData.gender == ''
      ? (this.formUpdateData.gender = null)
      : (this.formUpdateData.gender = this.formUpdateData.gender);
    const input = document.getElementById('profile-img') as HTMLInputElement;
    let file: File;
    file = input.files![0];
    if (file) {
      this.ngxService.startLoader('master');
      this.img.upload(file).subscribe((url) => {
        this.imageUrl = url;
        this.requestUpdate();
      });
    } else {
      this.imageUrl = null;
      this.requestUpdate();
    }
  }

  requestUpdate() {
    this.loginService
      .createUpdateRequest(
        this.formUpdateData,
        this.userData.username,
        this.imageUrl
      )
      .subscribe({
        next: (resp: any) => {
          this.updateBtnDisabled = true;
          this.toggleAlert = false;
          this.ngxService.stopLoader('master');
          if (resp == null) {
            this.toast.error({
              detail: 'Error',
              summary: `Error in Submitting Your Request`,
              duration: 2000,
            });
          } else {
            this.toast.success({
              detail: 'Success',
              summary: `Your Request is Submitted`,
              duration: 2000,
            });
          }
        },
        error: (err: any) => {
          this.ngxService.stopLoader('master');
          this.toast.error({
            detail: 'Error',
            summary: `Error in Submitting Your Request`,
            duration: 2000,
          });
        },
      });
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

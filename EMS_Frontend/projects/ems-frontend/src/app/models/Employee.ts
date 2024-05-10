import { Department } from './Department';
import { Paygrade } from './Paygrade';

export interface Employee {
  empID: number;
  firstName: string;
  lastName: string;
  dob: Date;
  gender: string;
  joiningDate: Date;
  personalEmail: string;
  companyEmail:string;
  status: string;
  mobileNo: string;
  role: string;
  street: string;
  city: string;
  state: string;
  pincode: string;
  paygrade: Paygrade;
  department: Department;
  project: Object;
  designation: Object;
}

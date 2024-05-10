package com.nri.ems.DTO;

import java.time.LocalDate;

import com.nri.ems.Utils.Enums.EmployementStatus;
import com.nri.ems.Utils.Enums.Gender;
import com.nri.ems.Utils.Enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDTO {

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private Gender gender;

    private LocalDate joiningDate;

    private String personalEmail;

    private String state;

    private String pincode;

    private String city;

    private String companyEmail;

    private EmployementStatus status;

    private String street;

    private String mobileNo;

    private Role role;

    private String paygrade;

    private String project;

    private String department;

    private String profileImgUrl;
    private String designation;
}

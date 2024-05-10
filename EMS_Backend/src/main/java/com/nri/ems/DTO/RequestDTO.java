package com.nri.ems.DTO;


import com.nri.ems.Entities.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestDTO {
  private Employee employee;
    private String requestedBy;
}

/*
 *    private String firstName;

    private String lastName;

    private LocalDate dob;

    private Gender gender;

    private LocalDate joiningDate;

    private String personalEmail;

    private String state;

    private String pincode;

 */
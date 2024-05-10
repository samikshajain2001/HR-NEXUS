package com.nri.ems.DTO;

import java.util.HashMap;

import lombok.Builder;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardDTO {

  Integer totalNoEmployee;
  Integer totalNoDepartment;
  Integer totalNoProject;
  Integer totalNoPaygrade;
  HashMap<String, Integer> empDepartment;
  HashMap<String, Integer> empGender;
  HashMap<String, Integer> empPaygrade;
  HashMap<String, Integer> empProject;
  HashMap<String, Integer> yearwise;
  HashMap<String, Integer> empDesig;

}

package com.nri.ems.Entities;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Department {
  @Id
  @Column(name = "department_id")
  @SequenceGenerator(sequenceName = "dept_sequence", name = "dept_sequence", initialValue = 10000, allocationSize = 1)
  @GeneratedValue(generator = "dept_sequence", strategy = GenerationType.SEQUENCE)
  private Integer departmentId;

  @NotNull(message = "Deptartment name is required")
  @Size(max = 16, message = "Maximum length of Department name cannot exceed {max} characters")
  @Column(name = "name", unique = true)
  private String name;

  @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
  @Email(message = "Invalid email format")
  @NotBlank(message = "Email is required")
  @Column(name = "email")
  private String email;

  @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number format for India")
  @Size(max = 10)
  @NotBlank(message = "Mobile number is required")
  @Column(name = "mobile_no")
  private String mobileNo;

  // @NotBlank(message = "Number of employees is required")
  // @PositiveOrZero(message = "Number of employees cannot be negative")
  // @Column(name = "no_of_employee")
  // private Integer noOfEmployee;

  @OneToMany(mappedBy = "department")
  @JsonIgnore
  private List<Employee> employee;

  // mapping done for department and Project
  @OneToMany(mappedBy = "department")
  @JsonIgnore
  private List<Project> projects;

  @Override
  public String toString() {
    return "Department [departmentId=" + departmentId + ", name=" + name + ", email=" + email + ", mobileNo=" + mobileNo
        + "]";
  }
}
//
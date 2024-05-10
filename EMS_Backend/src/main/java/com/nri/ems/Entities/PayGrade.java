package com.nri.ems.Entities;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Entity
public class PayGrade {
  @Id
  @SequenceGenerator(sequenceName = "id", name = "id", initialValue = 100, allocationSize = 1)
  @GeneratedValue(generator = "id", strategy = GenerationType.SEQUENCE)
  private Integer id;
  @Column(unique = true)
  private String paygradeName;
  @NotNull(message = "Minimum pay cannot be null")
  @Min(value = 0, message = "Minimum pay must be a non-negative value")
  @Column(name = "min_pay")
  private Double minPay;

  @NotNull(message = "HRA cannot be null")
  @Min(value = 0, message = "HRA must be a non-negative value")
  @Max(value = 100, message = "HRA cannot exceed 100% of the salary")
  @Column(name = "hra")
  private Double hra;

  @NotNull(message = "PFS cannot be null")
  @Min(value = 0, message = "PFS must be a non-negative value")
  @Max(value = 100, message = "PFS cannot exceed 100% of the salary")
  @Column(name = "pfs")
  private Double pfs;

  @NotNull(message = "DA cannot be null")
  @Min(value = 0, message = "DA must be a non-negative value")
  @Max(value = 100, message = "DA cannot exceed 100% of the salary")
  @Column(name = "da")
  private Double da;

  @NotNull(message = "Maximum pay cannot be null")
  @Min(value = 0, message = "Maximum pay must be a non-negative value")
  @Column(name = "max_pay")
  private Double maxPay;

  @OneToMany(mappedBy = "paygrade")
  @JsonIgnore
  private List<Employee> employee;

  // @Override
  // public String toString() {
  // return "paygrade to string";
  // }
}

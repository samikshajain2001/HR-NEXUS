package com.nri.ems.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nri.ems.DTO.Authority;
import com.nri.ems.Utils.Enums.EmployementStatus;
import com.nri.ems.Utils.Enums.Gender;
import com.nri.ems.Utils.Enums.Role;

import java.time.LocalDate;
import java.util.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Entity
@Table(name = "EMP_TABLE")
public class Employee implements UserDetails {
    @Id
    @SequenceGenerator(sequenceName = "id_sequence", name = "id_sequence", initialValue = 100, allocationSize = 1)
    @GeneratedValue(generator = "id_sequence", strategy = GenerationType.SEQUENCE)
    @Column(name = "emp_id")
    // @PositiveOrZero(message = "Employee ID must be a positive number or zero")
    private Integer empID;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false)
    @Size(max = 18, message = "First name exceeds limit")
    private String firstName;

    @Column(name = "last_name")
    @Size(max = 18, min = 0, message = "Last name exceeds limit")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull(message = "Joining date is required")
    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;
    // @Column(name="joining_date")

    @Email(message = "Invalid email format")
    @NotBlank(message = "Personal email is required")
    @Column(name = "personal_email", nullable = false, unique = true)
    // @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
    // message = "Invalid email format")
    @Pattern(regexp = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$", message = "Invalid email format")

    private String personalEmail;

    @Column(name = "state")
    private String state;

    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid PIN code format")
    @Column(name = "pincode")
    private String pincode;

    // @NotBlank(message = "City is required")
    @Column(name = "city")
    private String city;

    @Pattern(regexp = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$", message = "Invalid email format")
    @Email(message = "Invalid email format")
    // @NotBlank(message = "Company email is required")
    @Column(name = "company_email")
    private String companyEmail;

    @Enumerated(EnumType.STRING)
    private EmployementStatus status;

    // @NotBlank(message = "Street is required")
    @Column(name = "street")
    private String street;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number format for India")
    @NotBlank(message = "Mobile number is required")
    @Column(name = "mobile_no")
    private String mobileNo;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Size(max = 19)
    // @NotNull(message = "Username is required")
    @Column(name = "username")
    private String username;

    // @NotNull(message = "Password is required")
    @Column(name = "password")
    private String password;

    // Mapping done for the employee and paygrade
    @ManyToOne
    @JoinColumn(name = "id")

    private PayGrade paygrade;

    // Mapping done for the employee and department
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.REMOVE)
    @JsonIgnore
    RefreshToken refreshToken;

    private String profileImgUrl;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "designation_id")
    private Designation designation;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority(this.getRole().name()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public String toString() {
        return "Employee [empID=" + empID + ", firstName=" + firstName + ", lastName=" + lastName + ", ...]";
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

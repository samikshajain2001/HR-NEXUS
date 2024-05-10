package com.nri.ems.Entities;

import java.time.LocalDate;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.nri.ems.Utils.Enums.Gender;
import com.nri.ems.Utils.Enums.RequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRequest {
    @Id
    @SequenceGenerator(sequenceName = "request_sequence", name = "request_sequence", initialValue = 100, allocationSize = 1)
    @GeneratedValue(generator = "request_sequence", strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String firstName;
    private String lastName;
    private String mobileNo;
    private LocalDate dob;
    private LocalDate joiningDate;
    private String companyEmail;
    private String personalEmail;
    private String street;
    private String city;
    private String pincode;
    private String state;
    private Gender gender;
    private String profileImgUrl;

    // username of employee
    private String requestedBy;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(nullable = false)
    private Date updatedAt;
    // username of Admin
    private String updatedBy;

}

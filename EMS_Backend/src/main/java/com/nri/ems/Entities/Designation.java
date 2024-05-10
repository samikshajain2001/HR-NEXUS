package com.nri.ems.Entities;

import java.util.List;

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

@Entity

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Designation {

    @Id
    @Column(name = "designation_id")
    @SequenceGenerator(sequenceName = "designation_sequence", name = "designation_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "designation_sequence", strategy = GenerationType.SEQUENCE)
    private Integer designationId;
    private String title;
    private String description;
    @OneToMany(mappedBy = "designation")
    @JsonIgnore
    private List<Employee> employee;

}

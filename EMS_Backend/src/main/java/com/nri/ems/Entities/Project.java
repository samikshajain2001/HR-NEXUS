package com.nri.ems.Entities;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nri.ems.Utils.Enums.ProjectStatus;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Entity
public class Project {
    @Id
    @SequenceGenerator(sequenceName = "project_id", name = "project_id", allocationSize = 1)
    @GeneratedValue(generator = "project_id", strategy = GenerationType.SEQUENCE)
    private Integer projectId;

    @NotNull(message = "Project title is required")
    @Size(max = 255, message = "Project title cannot exceed 255 characters")
    @Column(name = "project_title")
    private String projectTitle;

    @Column(name = "project_status")
    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    // @Column(name = "no_employee")
    // private Integer noEmployee;

    @Column(name = "projectDesc")
    private String projectDesc;

    @NotNull(message = "Project date is required")
    @Column(name = "start_date")
    private LocalDate start_date;
    @NotNull(message = "Project date is required")
    @Column(name = "end_date")
    private LocalDate end_date;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private List<Employee> employees;

    // mapping for project and department
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}

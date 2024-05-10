package com.nri.ems.DTO;

import java.time.LocalDate;

import com.nri.ems.Utils.Enums.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDTO {
    private String projectTitle;
    private ProjectStatus projectStatus;
    private String projectDesc;
    private LocalDate start_date;
    private LocalDate end_date;
    private String department;
}

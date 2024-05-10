package com.nri.ems.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.nri.ems.DTO.ProjectDTO;
import com.nri.ems.Entities.Department;
import com.nri.ems.Entities.Employee;
import com.nri.ems.Entities.Project;
import com.nri.ems.Utils.Enums.ProjectStatus;

public interface ProjectService {
    Project createProject(ProjectDTO project);

    List<Project> getAllProjects();

    Optional<Project> getById(int id);

    boolean deleteProjById(Integer id);

    Project updateById(Integer id, ProjectDTO updateProject);

    Page<Project> getAllProjectsByPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    Long countProject();

    String assignDepartment(String projectTitle, String name);

    public Page<Project> searchProjectByTitle(Integer pageNumber, Integer pageSize, String projectTitle, String sortBy,
            String sortDir);

    Page<Project> filterByProjectStatus(ProjectStatus projectStatus, Integer pageNo, Integer pageSize);

    List<Project> getProjectsByDepartment(Department department);

    Page<Project> filterByProjectStatusAndDepartment(String projectStatus,
            String deptName, Integer pageNumber, Integer pageSize);

    List<Employee> getEmployeesProject(String title);

}

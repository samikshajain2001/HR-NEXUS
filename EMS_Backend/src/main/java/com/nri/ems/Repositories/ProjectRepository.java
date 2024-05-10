package com.nri.ems.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nri.ems.Entities.Department;
import com.nri.ems.Entities.Project;
import com.nri.ems.Utils.Enums.ProjectStatus;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
        Project findByProjectTitle(String projectTitle);

        // find the project by title starting with the title with pagination--Search
        Page<Project> findByprojectTitleStartsWithIgnoreCase(PageRequest pageData, String projectTitle);

        Page<Project> findByProjectStatus(ProjectStatus projectStatus, Pageable pageable);

        List<Project> findByDepartment(Department department);

        Page<Project> findByDepartment(Department department, Pageable page);

        List<Project> findByProjectStatusAndDepartment(ProjectStatus status,
                        Department department);

        Page<Project> findByProjectStatusAndDepartment(ProjectStatus status,
                        Department department, Pageable page);

        List<Project> findByProjectStatus(ProjectStatus status);

        @Query(value = "SELECT COUNT(*) FROM Project WHERE project_status='ONGOING'", nativeQuery = true)
        Integer findCountofActiveProjects();

        @Query(value = "SELECT COUNT(*) FROM Project", nativeQuery = true)
        Integer findTotalProjects();
}

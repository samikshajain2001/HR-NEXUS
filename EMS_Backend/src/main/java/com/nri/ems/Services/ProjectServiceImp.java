package com.nri.ems.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nri.ems.DTO.ProjectDTO;
import com.nri.ems.Entities.Department;
import com.nri.ems.Entities.Employee;
import com.nri.ems.Entities.Project;
import com.nri.ems.Exceptions.EmptyInputException;
import com.nri.ems.Repositories.DepartmentRepository;
import com.nri.ems.Repositories.ProjectRepository;
import com.nri.ems.Utils.Enums.ProjectStatus;

@Service
public class ProjectServiceImp implements ProjectService {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    private Project setProjectFromDto(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setProjectTitle(projectDTO.getProjectTitle());
        project.setProjectStatus(projectDTO.getProjectStatus());
        project.setProjectDesc(projectDTO.getProjectDesc());
        project.setStart_date(projectDTO.getStart_date());

        if (projectDTO.getProjectStatus() != null) {
            if (projectDTO.getProjectStatus().name().equals("ONGOING")
                    || projectDTO.getProjectStatus().name().equals("ONGOING")) {
                project.setEnd_date(null);
            } else {
                project.setEnd_date(projectDTO.getEnd_date());
            }
        }
        if (projectDTO.getDepartment() != null && !projectDTO.getDepartment().isEmpty()) {
            Department department = departmentRepository.findByName(projectDTO.getDepartment());
            if (department != null) {
                project.setDepartment(department);
            }
        }
        return project;
    }

    @Override
    public Project createProject(ProjectDTO projectDTO) {
        Project project = this.setProjectFromDto(projectDTO);
        if (project != null) {
            Project existingProject = projectRepository.findByProjectTitle(project.getProjectTitle());
            if (existingProject == null) {
                return projectRepository.save(project);
            } else {
                throw new EmptyInputException("Project with this title already exists,");
            }
        } else {
            return null;
        }
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Page<Project> getAllProjectsByPage(Integer pageNumber, Integer pageSize,
            String sortBy, String sortDir) {
        Sort sort = null;
        if (sortDir == "asc") {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }
        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Project> pageProjects = this.projectRepository.findAll(p);

        return pageProjects;
    }

    @Override
    public Optional<Project> getById(int id) {
        return projectRepository.findById(id);
    }

    @Override
    public boolean deleteProjById(Integer id) {
        if (id == null) {
            return false;
        }
        Optional<Project> projectOptional = projectRepository.findById(id);

        if (projectOptional.isEmpty()) {
            return false;
        } else {
            Project existProject = projectOptional.get();
            if (existProject != null) {
                if (existProject.getEmployees().size() > 0) {
                    throw new EmptyInputException("Project cannot be deleted,");
                } else
                    projectRepository.delete(existProject);
                return true;
            } else {
                return false;
            }
        }
    }

    Project updateProjectDetails(Project existingProject, ProjectDTO updatedProject) {

        if (updatedProject.getProjectTitle() != null && updatedProject.getProjectTitle().length() != 0) {
            Project projectTitleExists = projectRepository.findByProjectTitle(updatedProject.getProjectTitle());
            if (projectTitleExists != null) {
                throw new EmptyInputException("Project with this title already exists,");
            } else
                existingProject.setProjectTitle(updatedProject.getProjectTitle());
        }
        if (updatedProject.getProjectStatus() != null) {
            existingProject.setProjectStatus(updatedProject.getProjectStatus());
        }
        // if (updatedProject.getNoEmployee() != 0) {
        // existingProject.setNoEmployee(updatedProject.getNoEmployee());
        // }
        if (updatedProject.getStart_date() != null) {
            existingProject.setStart_date(updatedProject.getStart_date());
        }
        if (updatedProject.getEnd_date() != null) {
            if (updatedProject.getProjectStatus() != null) {
                if (updatedProject.getProjectStatus().name().equals("UPCOMING")
                        || updatedProject.getProjectStatus().name().equals("ONGOING")) {
                    existingProject.setEnd_date(null);
                }
            } else {
                existingProject.setEnd_date(updatedProject.getEnd_date());
            }
        }
        if (updatedProject.getProjectDesc() != null) {
            existingProject.setProjectDesc(updatedProject.getProjectDesc());
        }
        if (updatedProject.getDepartment() != null) {
            Department department = departmentRepository.findByName(updatedProject.getDepartment());
            if (department != null) {
                existingProject.setDepartment(department);
            }
        }
        return existingProject;
    }

    @Override
    public Project updateById(Integer id, ProjectDTO updatedProject) {
        if (id == null) {
            return null;
        }
        Project existingProject = projectRepository.findById(id).orElse(null);
        if (existingProject != null) {
            existingProject = this.updateProjectDetails(existingProject, updatedProject);
            if (existingProject != null) {
                return projectRepository.save(existingProject);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Long countProject() {
        return projectRepository.count();
    }

    // assing department to Project
    @Override
    public String assignDepartment(String projectTitle, String name) {
        Project p = projectRepository.findByProjectTitle(projectTitle);
        Department d = departmentRepository.findByName(name);

        if (p != null && d != null) {

            p.setDepartment(d);
            List<Project> project_lst = new ArrayList<>();
            project_lst.add(p);
            d.setProjects(project_lst);
            projectRepository.save(p);
            departmentRepository.save(d);

            return "Department Assigned Succefully";
        }
        return "Check details you have filled";

    }

    @Override
    public Page<Project> searchProjectByTitle(Integer pageNumber, Integer pageSize,
            String projectTitle, String sortBy, String sortDir) {
        Sort sort = null;
        if (sortDir == "desc") {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }
        PageRequest p = PageRequest.of(pageNumber, pageSize, sort);
        return projectRepository.findByprojectTitleStartsWithIgnoreCase(p, projectTitle);
    }

    @Override
    public Page<Project> filterByProjectStatus(ProjectStatus projectStatus, Integer pageNo, Integer pageSize) {
        Pageable page = PageRequest.of(pageNo, pageSize);
        return projectRepository.findByProjectStatus(projectStatus, page);
    }

    @Override
    public List<Project> getProjectsByDepartment(Department department) {
        return projectRepository.findByDepartment(department);
    }

    @Override
    public Page<Project> filterByProjectStatusAndDepartment(String projectStatus,
            String deptName, Integer pageNumber, Integer pageSize) {
        Page<Project> projects = null;
        PageRequest page = PageRequest.of(pageNumber, pageSize);
        if ((projectStatus != null && !projectStatus.equals("null"))
                && (deptName != null && !deptName.equals("null"))) {
            Department department = departmentRepository.findByName(deptName);
            ProjectStatus status = ProjectStatus.valueOf(projectStatus);
            projects = projectRepository.findByProjectStatusAndDepartment(status, department, page);
        } else if (projectStatus != null && !projectStatus.equals("null")) {
            ProjectStatus status = ProjectStatus.valueOf(projectStatus);
            projects = projectRepository.findByProjectStatus(status, page);
        } else if (deptName != null && !deptName.equals("null")) {
            Department department = departmentRepository.findByName(deptName);
            projects = projectRepository.findByDepartment(department, page);
        }
        return projects;
    }

    @Override
    public List<Employee> getEmployeesProject(String title) {
        Project existProject = projectRepository.findByProjectTitle(title);
        return existProject.getEmployees();
    }

}

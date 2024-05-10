package com.nri.ems.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nri.ems.DTO.ProjectDTO;
import com.nri.ems.Entities.Department;
import com.nri.ems.Entities.Employee;
import com.nri.ems.Entities.Project;
import com.nri.ems.Services.DepartmentService;
import com.nri.ems.Services.ProjectService;
import com.nri.ems.Utils.Enums.ProjectStatus;

@RestController
@CrossOrigin
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @Autowired
    DepartmentService departmentService;

    @GetMapping("/get-all")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/page-all")
    public Page<Project> getAllProject(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "projectId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        return projectService.getAllProjectsByPage(pageNumber, pageSize, sortBy, sortDir);
    }

    @PostMapping("/create")
    public Project createProject(@RequestBody ProjectDTO project) {
        return projectService.createProject(project);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable("id") int id) {
        Optional<Project> project = projectService.getById(id);

        if (project == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(project.get(), HttpStatus.OK);
        }
    }

    @DeleteMapping("/del-id/{id}")
    public ResponseEntity<Project> deleteProjectById(@PathVariable("id") Integer id) {
        Boolean project = projectService.deleteProjById(id);
        if (!project) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PutMapping("/upd-id/{id}")
    public ResponseEntity<Project> updateProjectById(@RequestBody ProjectDTO updateProject,
            @PathVariable("id") Integer id) {
        Project project = projectService.updateById(id, updateProject);
        if (project == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {

            return new ResponseEntity<>(project, HttpStatus.OK);
        }
    }

    @GetMapping("/search-name")
    public Page<Project> searchProject(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "projectTitle", defaultValue = "", required = false) String projectTitle,
            @RequestParam(value = "sortBy", defaultValue = "projectId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        return projectService.searchProjectByTitle(pageNumber, pageSize, projectTitle, sortBy, sortDir);
    }

    @GetMapping("/getProjectByStatus")
    public Page<Project> filterByProjectStatus(@RequestParam ProjectStatus projectStatus,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam Integer pageSize) {
        return projectService.filterByProjectStatus(projectStatus, pageNumber, pageSize);
    }

    // @GetMapping("/getProjectByDepartment")
    // public Page<Project> filterByDepartment(@RequestParam Department department,
    // @RequestParam(value = "pageNumber", defaultValue = "0", required = false)
    // Integer pageNumber,
    // @RequestParam Integer pageSize) {
    // return projectService.filterByDepartment(department, pageNumber, pageSize);
    // }

    @GetMapping("/by-department/{departmentId}")
    public ResponseEntity<List<Project>> getProjectsByDepartment(@PathVariable Integer departmentId) {
        // Retrieve the department from the departmentId
        Department department = departmentService.getById(departmentId); // Retrieve department from database based on
                                                                         // departmentId
        if (department == null) {
            return ResponseEntity.notFound().build();
        }

        // Fetch projects by department
        List<Project> projects = projectService.getProjectsByDepartment(department);
        return ResponseEntity.ok(projects);
    }
    @GetMapping("/byDepartmentAndStatus")
    public Page<Project> filterProjectsByStatusAndDepartment(
            @RequestParam(value = "projectStatus", required = false) String projectStatus,
            @RequestParam(value = "deptName", required = false) String deptName,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize
            ) {
        return projectService.filterByProjectStatusAndDepartment(projectStatus, deptName,pageNumber,pageSize);
    }
    @GetMapping("/get-all-Employees")
    public List<Employee> filterByProjectTitle(@RequestParam String projectTitle) {
        return projectService.getEmployeesProject(projectTitle);
    }

}

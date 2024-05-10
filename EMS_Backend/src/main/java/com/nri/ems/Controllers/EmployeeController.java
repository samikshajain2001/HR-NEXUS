package com.nri.ems.Controllers;

import java.security.Principal;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import com.nri.ems.DTO.ChangePassDTO;
import com.nri.ems.DTO.DashboardDTO;
import com.nri.ems.DTO.EmployeeDTO;
import com.nri.ems.DTO.Reports.ReportResponse;
import com.nri.ems.Entities.Designation;
import com.nri.ems.Entities.Employee;
import com.nri.ems.Entities.UpdateRequest;
import com.nri.ems.Services.DepartmentService;
import com.nri.ems.Services.DesignationService;
import com.nri.ems.Services.EmployeeService;
import com.nri.ems.Services.ProjectService;
import com.nri.ems.Services.ReportService;
import com.nri.ems.Services.UpdateRequestService;
import com.nri.ems.Utils.Enums.EmployementStatus;
import com.nri.ems.Utils.Enums.RequestStatus;

import jakarta.mail.MessagingException;
import net.sf.jasperreports.engine.JRException;

@CrossOrigin
@RestController
@RequestMapping("/hr")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    DepartmentService departmentService;
    @Autowired
    ProjectService projectService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    UpdateRequestService updateRequestService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping("/get-all")
    public List<Employee> getAllEmps() {
        return employeeService.getAllEmployees();
    }

    // get list of employees page wise
    @GetMapping("/page-all")
    public Page<Employee> getAllEmployees(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "empID", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        return employeeService.getAllEmployeesByPage(pageNumber, pageSize, sortBy, sortDir);
    }

    // create employee
    @PostMapping("/createEmployee")
    public Employee createEmployee(@RequestBody EmployeeDTO employee) throws MessagingException {
        return employeeService.createEmployee(employee);
    }

    // get employee by employee id
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) {
        Employee optEmp = employeeService.getById(id);
        if (optEmp == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(optEmp, HttpStatus.OK);

        }
    }

    // api to get a specific employee
    @GetMapping("/get/{username}")
    public ResponseEntity<Employee> getEmployeeByusername(@PathVariable String username) {
        Employee existEmployee = employeeService.getByUsername(username);
        if (existEmployee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(existEmployee, HttpStatus.OK);
        }
    }

    // api to delete an employee
    @DeleteMapping("/delete-id/{id}")
    public ResponseEntity<Employee> deleteEmpById(@PathVariable Integer id) {
        boolean isdeleted = employeeService.deleteEmpById(id);
        if (!isdeleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Employee> deleteByUsername(@PathVariable String username) {
        boolean isdeleted = employeeService.deleteByUsername(username);
        if (!isdeleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    // update employee by employee id
    @PutMapping("/update-id/{id}")
    public ResponseEntity<Employee> updateById(Principal principal, @PathVariable Integer id,
            @RequestBody EmployeeDTO updateEmp) {
        Employee requestEmp = (Employee) userDetailsService.loadUserByUsername(principal.getName());
        Employee updatedEmp = employeeService.updateById(requestEmp, id, updateEmp);

        if (updatedEmp == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(updatedEmp, HttpStatus.OK);
        }
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<Employee> updateByUsername(Principal principal, @PathVariable String username,
            @RequestBody EmployeeDTO updateEmp) {
        Employee requestEmp = (Employee) userDetailsService.loadUserByUsername(principal.getName());
        Employee updatedEmp = employeeService.updatebyUsername(requestEmp.getEmpID(), username, updateEmp);
        if (updatedEmp == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(updatedEmp, HttpStatus.OK);
        }
    }

    // search employee
    @GetMapping("/search")
    public Page<Employee> searchByFullname(
            @RequestParam(value = "firstName", defaultValue = "", required = false) String firstName,
            @RequestParam(value = "lastName", defaultValue = "", required = false) String lastName,
            @RequestParam(value = "sortBy", defaultValue = "empID", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir,
            @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        return employeeService.searchEmployeesByFullName(firstName, lastName, pageNo, pageSize, sortBy, sortDir);
    }

    // count total no department
    @GetMapping("/totalDep")
    public Long countDep() {
        return departmentService.countDepartment();
    }

    // count total no project
    @GetMapping("/totalProj")
    public Long countProj() {
        return projectService.countProject();
    }

    // get dashboard data
    @GetMapping("/getDashboard")
    public ResponseEntity<DashboardDTO> getDashboard() {
        Integer totalemployees = employeeService.countEmployee();
        Integer totalprojects = employeeService.countProject();
        Integer totaldepartments = employeeService.countDepartment();
        Integer totalpaygrades = employeeService.countPaygrde();
        HashMap<String, Integer> gendermp = employeeService.getGenderData();
        HashMap<String, Integer> departmp = employeeService.getEmployeeCountByDepartment();
        HashMap<String, Integer> desigmp = employeeService.getEmployeeCountByDesignation();
        HashMap<String, Integer> projmp = employeeService.getEmployeeCountByProject();
        HashMap<String, Integer> paymp = employeeService.getEmployeeCountByPaygrade();
        HashMap<String, Integer> yearwise = employeeService.getYearWiseData();
        return ResponseEntity.ok(DashboardDTO.builder()
                .totalNoEmployee(totalemployees)
                .totalNoProject(totalprojects)
                .totalNoDepartment(totaldepartments)
                .totalNoPaygrade(totalpaygrades)
                .empGender(gendermp)
                .empDepartment(departmp)
                .empProject(projmp)
                .empPaygrade(paymp)
                .yearwise(yearwise)
                .empDesig(desigmp)
                .build());

    }

    @GetMapping("/getEmpByStatus")
    public Page<Employee> filterByStatus(@RequestParam EmployementStatus status, @RequestParam Integer pageNo,
            @RequestParam Integer pageSize) {
        return employeeService.filterByStatus(status, pageNo, pageSize);
    }

    @PostMapping("/update-request")
    public UpdateRequest updateStatus(Principal principal, @RequestParam String status, @RequestParam Integer id)
            throws MessagingException {
        RequestStatus currStatus = null;
        if (status.equals("APPROVED")) {
            currStatus = RequestStatus.APPROVED;
        } else {
            currStatus = RequestStatus.REJECTED;
        }
        Employee requestEmp = (Employee) userDetailsService.loadUserByUsername(principal.getName());
        return updateRequestService.updateStatus(currStatus, id, requestEmp.getRole().name(), requestEmp.getUsername());
    }

    @GetMapping("/generateReport")
    public ResponseEntity<ReportResponse> generateReport(@RequestParam String reportType,
            @RequestParam String format,
            @RequestParam Integer empId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String deptName) throws FileNotFoundException, JRException {

        switch (reportType) {
            case "basic":
                return reportService.exportBasicReport(format, empId);
            case "employee":
                return reportService.exportReport(format, empId, status, gender, deptName);

            case "project":
                return reportService.exportProReport(format, empId, status, deptName);

            default:
                break;
        }
        return null;
    }

    @GetMapping("/getAllRequests")
    public Page<UpdateRequest> getAllRequests(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        return updateRequestService.getAllRequest(pageNo, pageSize);
    }

    @GetMapping("getAllDesignations")
    public List<Designation> getDesignations() {
        return designationService.getAllDesignations();
    }

    @PostMapping("/createDesignation")
    public Designation createDesignation(@RequestBody Designation designation) {
        return designationService.createDesignation(designation);
    }

    @DeleteMapping("/deleteDesignation/{id}")
    public ResponseEntity<Designation> deleteByUsername(@PathVariable Integer id) {
        boolean isdeleted = designationService.deleteDesById(id);
        if (!isdeleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PutMapping("/updateDesignation/{id}")
    public ResponseEntity<Designation> updateById(@PathVariable Integer id, @RequestBody Designation designation) {
        Designation designation2 = designationService.updateById(id, designation);

        if (designation2 == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(designation2, HttpStatus.OK);
        }
    }

    @PostMapping("/newpassword")
    public ResponseEntity<String> changepassword(@RequestBody ChangePassDTO changePassword, Principal principal) {
        Employee requestEmp = (Employee) userDetailsService.loadUserByUsername(principal.getName());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestEmp.getUsername(), changePassword.getOldpassword()));
        } catch (Exception e) {

            return new ResponseEntity<>("Invalid Credentials", HttpStatus.CONFLICT);
        }

        String encodedpass = passwordEncoder.encode(changePassword.getPassword());
        int a = employeeService.updatepass(requestEmp.getUsername(), encodedpass);
        if (a == 1)
            return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
        else {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getEmpByDepartment")
    public List<Employee> filterByDept(@RequestParam String deptName) {
        return employeeService.filterByDepartment(deptName);
    }

}

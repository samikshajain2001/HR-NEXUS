package com.nri.ems.Services;

import com.nri.ems.Config.JWT.JWTService;
import com.nri.ems.DTO.AuthRequest;
import com.nri.ems.DTO.AuthResponse;
import com.nri.ems.DTO.EmployeeDTO;
import com.nri.ems.Entities.*;
import com.nri.ems.Exceptions.EmptyInputException;
import com.nri.ems.Exceptions.ResourceNotFoundException;
import com.nri.ems.Exceptions.UnAuthorizedAccessException;
import com.nri.ems.Repositories.DepartmentRepository;
import com.nri.ems.Repositories.DesignationRepository;
import com.nri.ems.Repositories.EmployeeRepository;
import com.nri.ems.Repositories.PayGradeRepository;
import com.nri.ems.Repositories.ProjectRepository;
import com.nri.ems.Utils.Enums.EmployementStatus;

import jakarta.mail.MessagingException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    MailService mailService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PayGradeRepository payGradeRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    JWTService jwtService;

    @Autowired
    DesignationRepository designationRepository;

    @Autowired
    RefreshTokenServiceImpl refreshTokenServiceImpl;

    @Autowired
    DepartmentRepository departmentRepository;

    public AuthResponse authenticate(AuthRequest authRequest) {
        // backend field validations
        if (authRequest.getUserName() == null || authRequest.getPassword() == null) {
            throw new EmptyInputException("username | password");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        } catch (Exception e) {
            throw new UnAuthorizedAccessException();
        }
        Employee employee = employeeRepository.findByUsername(authRequest.getUserName());
        if (employee == null) {
            throw new ResourceNotFoundException("Employee", "", "");
        } else {
            String token = jwtService.generateToken(employee);
            RefreshToken refreshToken = refreshTokenServiceImpl.createRefreshToken(employee.getUsername());
            return AuthResponse.builder()
                    .accesstoken(token)
                    .accesstokenexpiry(jwtService.extractExpiration(token))
                    .username(employee.getUsername())
                    .password(employee.getPassword())
                    .refreshtoken(refreshToken.getToken())
                    .refreshtokenExpiry(refreshToken.getExpiry())
                    .build();
        }
    }

    // optimizing create Employee function
    private Employee setEmployeeFromDto(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        if (employeeDTO.getCompanyEmail() != null
                && employeeDTO.getCompanyEmail().equals(employeeDTO.getPersonalEmail())) {
            throw new EmptyInputException("Company Email cannot be same as personal email,");
        }
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setStatus(employeeDTO.getStatus());
        employee.setLastName(employeeDTO.getLastName());
        employee.setDob(employeeDTO.getDob());
        employee.setGender(employeeDTO.getGender());
        employee.setJoiningDate(employeeDTO.getJoiningDate());
        if (employeeDTO.getPersonalEmail() != null && !employeeDTO.getPersonalEmail().isEmpty()) {
            if (employeeRepository.findByPersonalEmail(employeeDTO.getPersonalEmail()) == null) {
                employee.setPersonalEmail(employeeDTO.getPersonalEmail());

            } else {
                throw new EmptyInputException("Personal Email already exists,");
            }
        }
        if (employeeDTO.getCompanyEmail() != null && !employeeDTO.getCompanyEmail().isEmpty()) {
            if (employeeRepository.findByCompanyEmail(employeeDTO.getCompanyEmail()) == null) {
                employee.setCompanyEmail(employeeDTO.getCompanyEmail());

            } else {
                throw new EmptyInputException("Company Email already exists,");
            }

        }
        employee.setPincode(employeeDTO.getPincode());
        employee.setState(employeeDTO.getState());
        employee.setStreet(employeeDTO.getStreet());
        employee.setCity(employeeDTO.getCity());
        if (employeeDTO.getMobileNo() != null && !employeeDTO.getMobileNo().isEmpty()) {
            if (employeeRepository.findByMobileNo(employeeDTO.getMobileNo()) == null) {
                employee.setMobileNo(employeeDTO.getMobileNo());

            } else {

                throw new EmptyInputException("Mobile Number already exists,");
            }
        }
        employee.setRole(employeeDTO.getRole());

        employee.setProfileImgUrl(employeeDTO.getProfileImgUrl());
        if (employeeDTO.getDesignation() != null && !employeeDTO.getDesignation().isEmpty()) {
            Designation designation = designationRepository.findByTitle(employeeDTO.getDesignation());
            if (designation != null) {
                employee.setDesignation(designation);
            } else {
                throw new EmptyInputException("Designation Not Found,");
            }
        }

        if (employeeDTO.getDepartment() != null && !employeeDTO.getDepartment().isEmpty()) {
            Department department = departmentRepository.findByName(employeeDTO.getDepartment());
            if (department != null) {
                employee.setDepartment(department);
            } else {
                throw new EmptyInputException("Department Not Found,");
            }
        }
        if (employeeDTO.getProject() != null && !employeeDTO.getProject().isEmpty()) {
            Project project = projectRepository.findByProjectTitle(employeeDTO.getProject());
            if (project != null) {
                employee.setProject(project);
            } else {
                throw new EmptyInputException("Project Not Found,");

            }
        }
        if (employeeDTO.getPaygrade() != null && !employeeDTO.getPaygrade().isEmpty()) {
            PayGrade paygrade = payGradeRepository.findByPaygradeName(employeeDTO.getPaygrade());
            if (paygrade != null) {
                employee.setPaygrade(paygrade);
            } else {
                throw new EmptyInputException("Paygrade Not Found,");

            }
        }
        return employee;
    }

    private String genereatePassword(Employee employee) {
        List<Employee> allemployees = this.getAllEmployees();
        int sizeplusone = allemployees.size() + 1;
        String password = "Nri@#@" + employee.getFirstName().charAt(0) + employee.getLastName().charAt(0)
                + sizeplusone;
        return password;
    }

    private String generateUsername(Employee employee) {
        String baseUsername = employee.getFirstName().trim().toLowerCase()
                + employee.getLastName().trim().toLowerCase().charAt(0);
        String username = baseUsername;
        int suffix = 1;
        while (!usernameAvailable(username)) {
            username = baseUsername + suffix;
            suffix++;
        }
        return username;
    }

    @Override
    public Employee createEmployee(EmployeeDTO employeeDTO) throws MessagingException {
        // function to set employee details
        Employee employee = this.setEmployeeFromDto(employeeDTO);
        String username = this.generateUsername(employee);
        String password = this.genereatePassword(employee);
        System.out.println("Username, password" + " " + username + " " + password);
        employee.setUsername(username);
        employee.setPassword(passwordEncoder.encode(password));
        // mail service
        String name = employee.getFirstName() + " " + employee.getLastName();
        mailService.sendMail(employee.getPersonalEmail(), name,
                employee.getUsername(), password);

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Page<Employee> getAllEmployeesByPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sortByDetails = null;
        if (sortDir.equalsIgnoreCase("asc")) {
            sortByDetails = Sort.by(sortBy).ascending();
        } else {
            sortByDetails = Sort.by(sortBy).descending();
        }
        Pageable p = PageRequest.of(pageNumber, pageSize, sortByDetails);
        Page<Employee> pageEmployee = this.employeeRepository.findAll(p);
        return pageEmployee;
    }

    @Override
    public Employee getById(Integer id) {
        if (id == null) {
            return null;
        } else
            return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    public Employee getByUsername(String username) {
        Employee existEmp = employeeRepository.findByUsername(username);
        if (existEmp == null) {
            throw new ResourceNotFoundException("User", "Username", username);
        } else {
            return existEmp;
        }
    }

    @Override
    public boolean deleteEmpById(Integer id) {
        if (id != null) {
            Employee existingEmployee = employeeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
            if (existingEmployee != null)
                employeeRepository.delete(existingEmployee);
            return true;
        } else {
            throw new EmptyInputException("Unable To Delete Employee,");
        }
    }

    @Override
    public boolean deleteByUsername(String usrername) {
        Employee foundEmp = employeeRepository.findByUsername(usrername);
        if (foundEmp != null) {
            employeeRepository.delete(foundEmp);
            return true;
        } else {
            throw new ResourceNotFoundException("User", "Username", usrername);
        }
    }

    // function to set the updated details.
    Employee setUpdatedDetails(Integer currId, Employee existingEmployee, EmployeeDTO updatedEmployee) {

        if (updatedEmployee.getFirstName() != null && !updatedEmployee.getFirstName().isEmpty()) {
            existingEmployee.setFirstName(updatedEmployee.getFirstName());
        }

        if (updatedEmployee.getLastName() != null && !updatedEmployee.getLastName().isEmpty()) {
            existingEmployee.setLastName(updatedEmployee.getLastName());
        }

        if (updatedEmployee.getCompanyEmail() != null && !updatedEmployee.getCompanyEmail().isEmpty()) {
            if (!(updatedEmployee.getCompanyEmail().equals(existingEmployee.getCompanyEmail()))) {
                if (employeeRepository.findByCompanyEmail(updatedEmployee.getCompanyEmail()) == null) {
                    existingEmployee.setCompanyEmail(updatedEmployee.getCompanyEmail());

                } else {
                    throw new EmptyInputException("Company Email already exists,");
                }
            }
        }
        if (updatedEmployee.getPersonalEmail() != null && !updatedEmployee.getPersonalEmail().isEmpty()) {
            if (!(updatedEmployee.getPersonalEmail().equals(existingEmployee.getPersonalEmail()))) {
                if (employeeRepository.findByPersonalEmail(updatedEmployee.getPersonalEmail()) == null) {
                    existingEmployee.setPersonalEmail(updatedEmployee.getPersonalEmail());
                } else {
                    throw new EmptyInputException("Personal Email already exists,");
                }
            }
        }
        if (updatedEmployee.getMobileNo() != null && !updatedEmployee.getMobileNo().isEmpty()) {
            if (!(updatedEmployee.getMobileNo().equals(existingEmployee.getMobileNo()))) {
                if (employeeRepository.findByMobileNo(updatedEmployee.getMobileNo()) == null) {
                    existingEmployee.setMobileNo(updatedEmployee.getMobileNo());
                } else {
                    throw new EmptyInputException("Mobile Number already exists,");
                }
            }
        }
        if (updatedEmployee.getDob() != null && !updatedEmployee.getDob().toString().isEmpty()) {
            existingEmployee.setDob(updatedEmployee.getDob());
        }
        if (updatedEmployee.getGender() != null && !updatedEmployee.getGender().toString().isEmpty()) {
            existingEmployee.setGender(updatedEmployee.getGender());
        }
        if (updatedEmployee.getStreet() != null && !updatedEmployee.getStreet().isEmpty()) {
            existingEmployee.setStreet(updatedEmployee.getStreet());
        }
        if (updatedEmployee.getCity() != null && !updatedEmployee.getCity().isEmpty()) {
            existingEmployee.setCity(updatedEmployee.getCity());
        }
        if (updatedEmployee.getPincode() != null && !updatedEmployee.getPincode().isEmpty()) {
            existingEmployee.setPincode(updatedEmployee.getPincode());
        }
        if (updatedEmployee.getState() != null && !updatedEmployee.getState().isEmpty()) {
            existingEmployee.setState(updatedEmployee.getState());
        }
        if (updatedEmployee.getJoiningDate() != null && !updatedEmployee.getJoiningDate().toString().isEmpty()) {
            existingEmployee.setJoiningDate(updatedEmployee.getJoiningDate());
        }

        if (updatedEmployee.getRole() != null && !updatedEmployee.getRole().toString().isEmpty()) {
            if (currId.equals(existingEmployee.getEmpID())) {
                throw new EmptyInputException("Cannot Change Your Own Role,");
            } else {
                existingEmployee.setRole(updatedEmployee.getRole());
            }
        }
        if (updatedEmployee.getDesignation() != null && !updatedEmployee.getDesignation().isEmpty()) {
            Designation designation = designationRepository.findByTitle(updatedEmployee.getDesignation());
            if (designation != null) {
                existingEmployee.setDesignation(designation);
            } else {
                throw new EmptyInputException("Designation Not Found,");

            }
        }

        if (updatedEmployee.getDepartment() != null && !updatedEmployee.getDepartment().isEmpty()) {
            Department department = departmentRepository.findByName(updatedEmployee.getDepartment());
            if (department != null) {
                existingEmployee.setDepartment(department);
            } else {
                throw new EmptyInputException("Department Not Found,");

            }
        }
        if (updatedEmployee.getProject() != null && !updatedEmployee.getProject().isEmpty()) {
            Project project = projectRepository.findByProjectTitle(updatedEmployee.getProject());
            if (project != null) {
                existingEmployee.setProject(project);
            } else {
                throw new EmptyInputException("Project Not Found,");

            }
        }
        if (updatedEmployee.getPaygrade() != null && !updatedEmployee.getPaygrade().isEmpty()) {
            PayGrade paygrade = payGradeRepository.findByPaygradeName(updatedEmployee.getPaygrade());
            if (paygrade != null) {
                existingEmployee.setPaygrade(paygrade);
            } else {
                throw new EmptyInputException("Paygrade Not Found,");

            }
        }

        if (updatedEmployee.getStatus() != null && !updatedEmployee.getStatus().toString().isEmpty()) {
            if (currId.equals(existingEmployee.getEmpID())) {
                throw new EmptyInputException("Cannot Change Your Own Status,");
            } else {

                if (!(updatedEmployee.getStatus().name().equals("ACTIVE"))) {
                    existingEmployee.setDepartment(null);
                    existingEmployee.setPaygrade(null);
                    existingEmployee.setProject(null);
                    existingEmployee.setDesignation(null);

                }
                existingEmployee.setStatus(updatedEmployee.getStatus());
            }
        }
        return existingEmployee;

    }

    @Override
    public Employee updateById(Employee requestEmp, Integer empID, EmployeeDTO updatedEmployee) {
        if (empID == null) {
            throw new ResourceNotFoundException(null, null, null);
        } else {
            Employee existingEmployee = employeeRepository.findById(empID)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", empID));
            // only root hr can change the details of HR
            if (existingEmployee.getRole().name().equals("ROLE_HR")) {
                if (!(requestEmp.getEmpID().equals(100) && requestEmp.getRole().name().equals("ROLE_HR"))) {
                    throw new EmptyInputException("Only Root HR can Change the details of another HR,");
                }
            }

            existingEmployee = this.setUpdatedDetails(requestEmp.getEmpID(), existingEmployee, updatedEmployee);
            if (existingEmployee != null)
                return employeeRepository.save(existingEmployee);
            else {
                throw new EmptyInputException("Employee Does Not Exists,");
            }
        }
    }

    @Override
    public Employee updatebyUsername(Integer currUser, String username, EmployeeDTO updatedEmployee) {
        Employee existingEmployee = employeeRepository.findByUsername(username);
        existingEmployee = setUpdatedDetails(currUser, existingEmployee, updatedEmployee);
        if (existingEmployee != null) {
            return employeeRepository.save(existingEmployee);
        } else {
            throw new ResourceNotFoundException("User", "Username", username);
        }
    }

    @Override
    public boolean usernameAvailable(String username) {
        Employee existEmployee = employeeRepository.findByUsername(username);
        if (existEmployee == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Page<Employee> searchEmployeeByusername(String firstName, Pageable p) {
        return employeeRepository.findByFirstNameStartingWithIgnoreCase(firstName, p);
    }

    @Override
    public Page<Employee> searchEmployeesByFullName(String firstName, String lastName, Integer pageNo,
            Integer pageSize, String sortBy, String sortDir) {

        Sort sortByDetails = null;
        if (sortDir.equalsIgnoreCase("asc")) {
            sortByDetails = Sort.by(sortBy).ascending();
        } else {
            sortByDetails = Sort.by(sortBy).descending();
        }
        Pageable p = PageRequest.of(pageNo, pageSize, sortByDetails);
        if (lastName != null && !lastName.isEmpty()) {
            return employeeRepository.findByFirstNameStartingWithIgnoreCaseAndLastNameStartingWithIgnoreCase(firstName,
                    lastName,
                    p);
        } else {
            return this.searchEmployeeByusername(firstName, p);
        }
    }

    // assingPaygrade
    @Override
    public String assignPaygrade(String userName, String paygradeName) {
        Employee e = employeeRepository.findByUsername(userName);
        PayGrade p = payGradeRepository.findByPaygradeName(paygradeName);

        if (e != null && p != null) {

            e.setPaygrade(p);
            List<Employee> emp_lst = new ArrayList<>();
            emp_lst.add(e);
            p.setEmployee(emp_lst);
            employeeRepository.save(e);
            payGradeRepository.save(p);

            return "PayGrade Assigned Succefully";
        }
        return "Check details you have filled";

    }

    // assing department to employee
    @Override
    public String assignDepartment(String userName, String name) {
        Employee e = employeeRepository.findByUsername(userName);
        Department d = departmentRepository.findByName(name);

        if (e != null && d != null) {

            e.setDepartment(d);
            List<Employee> emp_lst = new ArrayList<>();
            emp_lst.add(e);
            d.setEmployee(emp_lst);
            employeeRepository.save(e);
            departmentRepository.save(d);

            return "Department Assigned Succefully";
        }
        return "Check details you have filled";

    }

    // Data for dashboard
    @Override
    public Integer countEmployee() {
        return employeeRepository.findCountofActiveEmployees();
    }

    public Integer countDepartment() {
        return (int) departmentRepository.count();
    }

    @Override
    public Integer countProject() {
        return projectRepository.findCountofActiveProjects();
    }

    @Override
    public Integer countPaygrde() {
        return (int) payGradeRepository.count();
    }

    @Override
    public HashMap<String, Integer> getGenderData() {
        List<Object[]> empGender = employeeRepository.resultListfingender();

        HashMap<String, Integer> mp = new HashMap<>();
        mp.put("MALE", 0);
        mp.put("FEMALE", 0);
        for (Object[] ob : empGender) {
            String gender = (String) ob[0];
            BigDecimal countBigDecimal = (BigDecimal) ob[1];
            Integer count = countBigDecimal.intValue();
            mp.put(gender, count);
        }

        return mp;
    }

    @Override
    public HashMap<String, Integer> getEmployeeCountByDepartment() {
        List<Department> lst = departmentRepository.findAll();
        HashMap<String, Integer> mp = new HashMap<>();
        for (Department department : lst) {
            mp.put(department.getName(), 0);
        }
        for (Department department : lst) {
            mp.put(department.getName(), (Integer) department.getEmployee().size());
        }
        return mp;
    }

    @Override
    public HashMap<String, Integer> getEmployeeCountByDesignation() {
        List<Designation> lst = designationRepository.findAll();
        HashMap<String, Integer> mp = new HashMap<>();
        for (Designation designation : lst) {
            mp.put(designation.getTitle(), 0);
        }
        for (Designation designation : lst) {
            mp.put(designation.getTitle(), (Integer) designation.getEmployee().size());
        }
        return mp;
    }

    @Override
    public HashMap<String, Integer> getEmployeeCountByProject() {
        HashMap<String, Integer> projectCounts = new HashMap<>();
        List<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            projectCounts.put(project.getProjectTitle(), 0);
        }
        for (Project project : projects) {
            projectCounts.put(project.getProjectTitle(), (Integer) project.getEmployees().size());
        }
        return projectCounts;
    }

    @Override
    public HashMap<String, Integer> getEmployeeCountByPaygrade() {
        HashMap<String, Integer> mp = new HashMap<>();
        List<PayGrade> lst = payGradeRepository.findAll();
        for (PayGrade paygrade : lst) {
            mp.put(paygrade.getPaygradeName(), 0);
        }
        for (PayGrade paygrade : lst) {
            mp.put(paygrade.getPaygradeName(), (Integer) paygrade.getEmployee().size());
        }
        return mp;
    }

    @Override
    public Page<Employee> filterByStatus(EmployementStatus status, Integer pageNo, Integer pageSize) {
        Pageable page = PageRequest.of(pageNo, pageSize);

        return employeeRepository.findByStatus(status, page);

    }

    @Override
    public List<Employee> filterByDepartment(String deptName) {
        Department department = departmentRepository.findByName(deptName);
        return employeeRepository.findByDepartment(department);
    }

    // assing Project to employee
    @Override
    public String assignProject(String userName, String projectTitle) {
        Employee e = employeeRepository.findByUsername(userName);
        Project p = projectRepository.findByProjectTitle(projectTitle);

        if (e != null && p != null) {

            e.setProject(p);
            List<Employee> emp_lst = new ArrayList<>();
            emp_lst.add(e);
            p.setEmployees(emp_lst);
            employeeRepository.save(e);
            projectRepository.save(p);

            return "Department Assigned Succefully";
        }
        return "Check details you have filled";

    }

    @Override
    public HashMap<String, Integer> getYearWiseData() {
        List<Employee> lst = employeeRepository.findAll();
        HashMap<String, Integer> mp = new HashMap<>();
        List<Object[]> empcount = employeeRepository.getCountOfEmployeesByJoiningYear();

        for (Employee emp : lst) {
            int year = emp.getJoiningDate().getYear();
            String yearString = String.valueOf(year);
            mp.put(yearString, 0);
        }
        for (Object ob[] : empcount) {
            Integer yearBigDecimal = (Integer) ob[0]; // Assuming the year is returned as a BigDecimal
            String year = yearBigDecimal.toString();
            Long countLong = (Long) ob[1];
            Integer count = countLong.intValue();

            mp.put(year, (Integer) count);
        }
        return mp;

    }

    public Employee updateDetails(Employee emp) {
        return employeeRepository.save(emp);
    }

    @Override
    public int updatepass(String username, String paassowrd) {
        return employeeRepository.updatepass(username, paassowrd);
    }

}

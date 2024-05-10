package com.nri.ems.Services;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nri.ems.DTO.AuthRequest;
import com.nri.ems.DTO.AuthResponse;
import com.nri.ems.DTO.EmployeeDTO;
import com.nri.ems.Entities.Employee;
import com.nri.ems.Utils.Enums.EmployementStatus;

import jakarta.mail.MessagingException;

public interface EmployeeService {
    Employee createEmployee(EmployeeDTO employee) throws MessagingException;

    List<Employee> getAllEmployees();

    Employee getById(Integer id);

    String assignDepartment(String userName, String name);

    String assignProject(String projectTitle, String name);

    String assignPaygrade(String userName, String payGradeName);

    Employee getByUsername(String username);

    boolean deleteEmpById(Integer id);

    boolean deleteByUsername(String usrername);

    Employee updateById(Employee requestEmp, Integer empID, EmployeeDTO updatedEmployee);

    Employee updatebyUsername(Integer currUser, String username, EmployeeDTO updatedEmployee);

    AuthResponse authenticate(AuthRequest authRequest);

    boolean usernameAvailable(String username);

    Page<Employee> getAllEmployeesByPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    Page<Employee> searchEmployeeByusername(String firstName, Pageable p);

    Page<Employee> searchEmployeesByFullName(String firstName, String lastName, Integer pageNo, Integer pageSize,
            String sortBy, String sortDir);

    Integer countEmployee();

    Integer countDepartment();

    Integer countProject();

    Integer countPaygrde();

    HashMap<String, Integer> getGenderData();

    HashMap<String, Integer> getEmployeeCountByProject();

    HashMap<String, Integer> getEmployeeCountByPaygrade();

    HashMap<String, Integer> getEmployeeCountByDepartment();

    HashMap<String, Integer> getEmployeeCountByDesignation();

    Page<Employee> filterByStatus(EmployementStatus status, Integer pageNo, Integer pageSize);

    HashMap<String, Integer> getYearWiseData();

    int updatepass(String username, String paassowrd);

    public List<Employee> filterByDepartment(String deptName);
}

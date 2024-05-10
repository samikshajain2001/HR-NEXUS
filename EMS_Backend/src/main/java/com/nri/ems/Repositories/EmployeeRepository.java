package com.nri.ems.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nri.ems.Entities.Department;
import com.nri.ems.Entities.Employee;
import com.nri.ems.Entities.PayGrade;
import com.nri.ems.Entities.Project;
import com.nri.ems.Utils.Enums.EmployementStatus;
import com.nri.ems.Utils.Enums.Gender;

import jakarta.transaction.Transactional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
        <Optional> Employee findByUsername(String username);

        Employee findByPersonalEmail(String personalEmail);

        Employee findByCompanyEmail(String companyEmail);

        Employee findByMobileNo(String mobileNo);

        Page<Employee> findByFirstNameContaining(String firstName, Pageable pageable);

        Page<Employee> findByFirstNameStartingWithIgnoreCase(String firstName, Pageable pageable);

        Page<Employee> findByFirstNameStartingWithIgnoreCaseAndLastNameStartingWithIgnoreCase(String firstName,
                        String lastName,
                        Pageable pageable);

        List<Employee> findByStatusAndGender(EmployementStatus status, Gender gender);

        @Query(value = "SELECT gender, COUNT(*) FROM emp_table WHERE status = 'ACTIVE' GROUP BY gender", nativeQuery = true)
        List<Object[]> resultListfingender();

        Page<Employee> findByStatus(EmployementStatus status, Pageable pageable);

        @Query("SELECT EXTRACT(YEAR FROM e.joiningDate), COUNT(e) " +
                        "FROM Employee e " +
                        "GROUP BY EXTRACT(YEAR FROM e.joiningDate) " +
                        "ORDER BY EXTRACT(YEAR FROM e.joiningDate) ASC")

        List<Object[]> getCountOfEmployeesByJoiningYear();

        List<Employee> findByStatusAndGenderAndDepartment(EmployementStatus status, Gender gender,
                        Department department);

        List<Employee> findByStatusAndDepartment(EmployementStatus status,
                        Department department);

        List<Employee> findByGenderAndDepartment(Gender gender,
                        Department department);

        List<Employee> findByStatus(EmployementStatus status);

        List<Employee> findByGender(Gender gender);

        List<Employee> findByDepartment(Department department);

        List<Employee> findByPaygrade(PayGrade paygradeName);

        @Modifying
        @Transactional
        @Query(value = "update emp_table set password=?2 where username=?1", nativeQuery = true)
        int updatepass(String username, String password);

        List<Employee> findByProject(Project project);

        @Query(value = "SELECT COUNT(*) FROM EMP_TABLE WHERE status='ACTIVE'", nativeQuery = true)
        Integer findCountofActiveEmployees();

        @Query(value = "SELECT COUNT(*) FROM EMP_TABLE WHERE status='RETIRE'", nativeQuery = true)
        Integer findCountofRetireEmployees();

        @Query(value = "SELECT COUNT(*) FROM EMP_TABLE WHERE status='LAYOFF'", nativeQuery = true)
        Integer findCountofLayoffEmployees();

        @Query(value = "SELECT COUNT(*) FROM EMP_TABLE WHERE status='RESIGN'", nativeQuery = true)
        Integer findCountofResignEmployees();

        @Query(value = "SELECT COUNT(*) FROM EMP_TABLE", nativeQuery = true)
        Integer findTotalEmployees();

}

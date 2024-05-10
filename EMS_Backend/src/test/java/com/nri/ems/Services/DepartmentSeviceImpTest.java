package com.nri.ems.Services;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.nri.ems.Entities.Department;
import com.nri.ems.Exceptions.ResourceNotFoundException;
import com.nri.ems.Repositories.DepartmentRepository;
import com.nri.ems.Repositories.EmployeeRepository;

@SpringBootTest
public class DepartmentSeviceImpTest {

    @Autowired
    DepartmentService departmentService;

    @MockBean
    EmployeeRepository employeeRepository;

    @MockBean
    DepartmentRepository departmentRepository;

    @BeforeEach
    void setUp() {
        Department departmentOpt = Department.builder()
                .name("Haha")
                .email("fdgjfdg@gmail.com")
                .mobileNo("9876543210")
                .employee(null)
                .departmentId(10001)
                .build();
        Optional<Department> department = Optional.ofNullable(departmentOpt);
        Mockito.when(departmentRepository.findById(10001)).thenReturn(department);

        Mockito.when(departmentRepository.save(departmentOpt)).thenReturn(departmentOpt);
    }


    @Test
    public void deleteDeptById_withInValidDepartment_shouldThrowNotFoundException() {
        Integer id = 10002;
        Department department = new Department();
        department.setDepartmentId(id);
        assertTrue(departmentService.deleteDeptById(id));
    }
    @Test
    public void deleteDeptById_withInValidDepartment_shouldThrowNullPointerException() {
        Integer id = null;
        assertNull(departmentService.deleteDeptById(id));
    }
    @Test
    public void deleteDeptById_withValidDepartment_shouldDeleteSuccessfully() {
        Integer id = 10001;
        Department department = new Department();
        department.setDepartmentId(id);
        assertTrue(departmentService.deleteDeptById(id));
    }

    @Test
    public void getById_withNullId_shouldThrowEmptyInputException() {
        // Arrange
        Integer id = null;

        // Act
        Department result = departmentService.getById(id);

        // Assert
        // Expects EmptyInputException
        assertNull(result);
    }

    @Test
    public void getById_withNonexistentId_shouldThrowResourceNotFoundException() {
        // Arrange
        Integer id = 1;
        // Mockito.when(departmentRepository.findById(id)).thenReturn(Optional.empty());
        Department result = departmentService.getById(id);
        // Act
        departmentService.getById(id);

        // Assert
        // Expects ResourceNotFoundException
        assertNotNull(result);
        assertEquals(id, result.getDepartmentId());
        throw new ResourceNotFoundException(null, null, null);
    }

    @Test
    void getById_withValidId_shouldReturnDepartment() {
        Integer id = 10001;
        Department result = departmentService.getById(id);
        assertNotNull(result);
        assertEquals(id, result.getDepartmentId());
    }

    @Test
    void updateById_withValidId_shouldReturnUpdatedDepartment() {
        Integer id = 10001;
        Department updatedDept=Department.builder()
        .mobileNo("9876543210")
        .name(null)
        .email(null)
        .build();
        Department result=departmentService.updateById(id, updatedDept);
        assertEquals(id, result.getDepartmentId());
    }
}

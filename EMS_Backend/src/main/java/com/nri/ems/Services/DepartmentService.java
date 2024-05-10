package com.nri.ems.Services;


import java.util.List;

import org.springframework.data.domain.Page;

import com.nri.ems.Entities.Department;

public interface DepartmentService {
    Department createDepartment(Department department);

    Department getById(Integer id);

    List<Department> getAllDepartments();

    // Optional<Department> getById(int id);

    boolean deleteDeptById(Integer id);

    Department updateById(Integer id, Department updateDepartment);

    public Page<Department> getAllDepartmentByPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    Long countDepartment();

    //HashMap<String, Integer> getEmployeeCountByDepartment();

    public Page<Department> searchDepartmentByName(Integer pageNumber, Integer pageSize, String name, String sortBy,
            String sortDir);

}

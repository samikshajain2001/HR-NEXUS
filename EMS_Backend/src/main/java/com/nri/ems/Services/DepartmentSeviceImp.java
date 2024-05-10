package com.nri.ems.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nri.ems.Entities.Department;
import com.nri.ems.Exceptions.EmptyInputException;
import com.nri.ems.Exceptions.ResourceNotFoundException;
import com.nri.ems.Repositories.DepartmentRepository;

@Service
public class DepartmentSeviceImp implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department createDepartment(Department department) {
        if (department.getName().isEmpty() || department.getName().length() == 0) {
            throw new EmptyInputException("Name");
        }
        if (department.getEmail().isEmpty() && department.getEmail().length() == 0) {
            throw new EmptyInputException("Email");
        }
        if (department.getMobileNo().isEmpty() && department.getMobileNo().length() == 0) {
            throw new EmptyInputException("Mobile Number");
        }
        Department exisDepartment = departmentRepository.findByNameIgnoreCase(department.getName());
        Department deptWithSameEmail = departmentRepository.findByEmail(department.getEmail());
        Department deptWithSameMobile = departmentRepository.findByMobileNo(department.getMobileNo());
        if (exisDepartment == null && deptWithSameEmail == null && deptWithSameMobile==null)
            return departmentRepository.save(department);
        else {
            throw new EmptyInputException("Department with this name ,email or mobile already exists,");
        }
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Page<Department> getAllDepartmentByPage(Integer pageNumber, Integer pageSize, String sortBy,
            String sortDir) {
        Sort sort = null;
        if (sortDir.equals("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }
        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Department> pageDepartment = this.departmentRepository.findAll(p);

        return pageDepartment;
    }

    @Override
    public Department getById(Integer id) {
        if (id == null) {
            throw new EmptyInputException("Id");
        }
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return dept;
    }

    @Override
    public boolean deleteDeptById(Integer id) {
        if (id == null) {
            throw new EmptyInputException("Id");
        }
        Department dept = departmentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if (dept == null) {
            throw new EmptyInputException("Dept");
        }
        // Integer empDeptCount = employeeRepository.findByDepartment(dept).size();
        if (dept.getEmployee()!=null && dept.getEmployee().size() > 0 ) {
            throw new EmptyInputException("Department cannot be deleted as there are employees associated,");
        }
        if(dept.getProjects()!=null && dept.getProjects().size() > 0){
            throw new EmptyInputException("Department cannot be deleted as there are Projects associated,");
        }
        departmentRepository.delete(dept);
        return true;
    }

    @Override
    public Department updateById(Integer id, Department updateDepartment) {
        if (id == null) {
            throw new EmptyInputException("Id");
        }
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if (updateDepartment.getName() != null && updateDepartment.getName().length() != 0) {
            Department existDept = departmentRepository.findByNameIgnoreCase(updateDepartment.getName());
            if (existDept != null) {
                throw new EmptyInputException("Department with this Name already exists,");
            } else
                dept.setName(updateDepartment.getName());
        }
        if (updateDepartment.getEmail() != null && updateDepartment.getEmail().length() != 0) {
            Department deptWithSameEmail = departmentRepository.findByEmail(updateDepartment.getEmail());
            if (deptWithSameEmail != null) {
                throw new EmptyInputException("Department with this Email already exists,");
            } else
                dept.setEmail(updateDepartment.getEmail());
        }
        if (updateDepartment.getMobileNo() != null && updateDepartment.getMobileNo().length() != 0) {
            dept.setMobileNo(updateDepartment.getMobileNo());
        }
        if (dept == null) {
            throw new EmptyInputException("Dept");
        }
        return departmentRepository.save(dept);
    }

    @Override
    public Long countDepartment() {
        return departmentRepository.count();

    }

    @Override
    public Page<Department> searchDepartmentByName(Integer pageNumber, Integer pageSize,
            String name, String sortBy, String sortDir) {
        Sort sort = null;
        if (sortDir.equals("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }
        PageRequest p = PageRequest.of(pageNumber, pageSize, sort);
        return departmentRepository.findByNameStartsWithIgnoreCase(p, name);
    }
}

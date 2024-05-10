package com.nri.ems.Controllers;

import java.util.List;

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

import com.nri.ems.Entities.Department;
import com.nri.ems.Services.DepartmentService;

@RestController
@CrossOrigin
@RequestMapping("/dept")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @GetMapping("/get-all")
    public List<Department> getAllDept() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/page-all")
    public Page<Department> getAllDepartment(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "departmentId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        return departmentService.getAllDepartmentByPage(pageNumber, pageSize, sortBy, sortDir);
    }

    @GetMapping("/search-name")
    public Page<Department> searchDepartment(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @RequestParam(value = "sortBy", defaultValue = "departmentId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        return departmentService.searchDepartmentByName(pageNumber, pageSize, name, sortBy, sortDir);
    }

    @PostMapping("/create")
    public Department createDept(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Department> getDeptById(@PathVariable("id") int id) {
        Department dept = departmentService.getById(id);
        if (dept == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(dept, HttpStatus.OK);
        }
    }

    @DeleteMapping("/del-id/{id}")
    public ResponseEntity<Department> deleteDeptById(@PathVariable("id") Integer id) {
        Boolean dept = departmentService.deleteDeptById(id);
        if (!dept) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PutMapping("/upd-id/{id}")
    public ResponseEntity<Department> updateDeptById(@PathVariable("id") Integer id,
            @RequestBody Department updateDepartment) {
        Department dept = departmentService.updateById(id, updateDepartment);
        if (dept == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {

            return new ResponseEntity<>(dept, HttpStatus.OK);
        }
    }

}

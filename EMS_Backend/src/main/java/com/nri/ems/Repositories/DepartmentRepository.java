package com.nri.ems.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.nri.ems.Entities.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Department findByName(String name);
    Department findByNameIgnoreCase(String name);

    Department findByEmail(String email);
    Department findByMobileNo(String mobileNo);

    Page<Department> findByNameStartsWithIgnoreCase(PageRequest pageData, String name);

}

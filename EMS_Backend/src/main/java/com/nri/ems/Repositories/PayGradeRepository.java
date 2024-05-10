package com.nri.ems.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nri.ems.Entities.PayGrade;

@Repository
public interface PayGradeRepository extends JpaRepository<PayGrade, Integer> {
    PayGrade findByPaygradeName(String name);

    Page<PayGrade> findByPaygradeNameStartsWithIgnoreCase(Pageable pageData, String name);

    PayGrade findByPaygradeNameStartsWithIgnoreCase(String name);

}

package com.nri.ems.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nri.ems.Entities.Designation;
import com.nri.ems.Entities.PayGrade;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Integer> {
    Designation findByTitle(String title);
}

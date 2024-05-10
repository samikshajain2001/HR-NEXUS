package com.nri.ems.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nri.ems.Entities.UpdateRequest;

@Repository
public interface RequestRepository extends JpaRepository<UpdateRequest, Integer> {
    UpdateRequest findByRequestedBy(String requestedBy);
}

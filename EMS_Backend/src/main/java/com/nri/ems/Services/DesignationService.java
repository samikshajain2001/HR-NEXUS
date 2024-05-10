package com.nri.ems.Services;

import java.util.List;

import com.nri.ems.Entities.Designation;

public interface DesignationService {

    List<Designation> getAllDesignations();

    Designation createDesignation(Designation designation);

    Designation updateById(Integer id, Designation designation);

    public boolean deleteDesById(Integer id);
}

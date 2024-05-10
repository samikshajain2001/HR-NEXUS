package com.nri.ems.Services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nri.ems.Entities.Designation;
import com.nri.ems.Exceptions.EmptyInputException;
import com.nri.ems.Exceptions.ResourceNotFoundException;
import com.nri.ems.Repositories.DesignationRepository;

@Service
public class DesignationServiceImpl implements DesignationService {

    @Autowired
    DesignationRepository designationRepository;

    @Override
    public List<Designation> getAllDesignations() {
        return designationRepository.findAll();
    }

    @Override
    public Designation createDesignation(Designation designation) {
        if (designation != null) {
            Designation existingDesignation = designationRepository.findByTitle(designation.getTitle());
            if (existingDesignation != null) {
                return null;
            }
            return designationRepository.save(designation);
        } else {
            return null;
        }
    }

    @Override
    public Designation updateById(Integer id, Designation designation) {
        if (id == null) {
            throw new EmptyInputException("Id");
        }
        Designation existingDesignation = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (existingDesignation != null) {

            if (!designation.getTitle().isEmpty()) {
                existingDesignation.setTitle(designation.getTitle());
            }
            if (!designation.getDescription().isEmpty()) {
                existingDesignation.setDescription(designation.getDescription());
                ;
            }

        }
        return designationRepository.save(existingDesignation);
    }

    @Override
    public boolean deleteDesById(Integer id) {
        if (id == null) {
            throw new EmptyInputException("Id");
        }
        Designation des = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if (des == null) {
            throw new EmptyInputException("Des");
        }
        if (des.getEmployee().size() > 0) {
            throw new EmptyInputException("Designation has some emplo");
        }
        designationRepository.delete(des);
        return true;
    }

}

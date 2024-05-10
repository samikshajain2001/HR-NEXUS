package com.nri.ems.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.nri.ems.Entities.Employee;
import com.nri.ems.Entities.PayGrade;

@Service
public interface PayGradeService {
    PayGrade createPayGrade(PayGrade payGrade);

    PayGrade getPaygrade(String name);

    List<PayGrade> getAllPayGrades();

    Optional<PayGrade> getById(int id);

    boolean deletePayGradeById(Integer id);

    PayGrade updateById(Integer id, PayGrade updateProject);

    public PayGrade add_combine(PayGrade p);

    Page<PayGrade> getAllPayGradesByPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    Page<PayGrade> searchPayGradeByName(Integer pageNumber, Integer pageSize, String paygradeName, String sortBy,
            String sortDir);

    List<Employee> filterByPaygrade(String paygradeName);            
}

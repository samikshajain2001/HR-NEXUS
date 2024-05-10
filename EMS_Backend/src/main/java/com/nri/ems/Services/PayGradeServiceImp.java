package com.nri.ems.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nri.ems.Entities.Employee;
import com.nri.ems.Entities.PayGrade;
import com.nri.ems.Exceptions.EmptyInputException;
import com.nri.ems.Repositories.EmployeeRepository;
import com.nri.ems.Repositories.PayGradeRepository;

@Service
public class PayGradeServiceImp implements PayGradeService {

    @Autowired
    private PayGradeRepository payGradeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    void existPaygrade(PayGrade existPay) {
        PayGrade existPayGrade = payGradeRepository.findByPaygradeName(existPay.getPaygradeName().trim());
        if (existPayGrade != null)
            throw new EmptyInputException("Paygrade with this name already exists,");
    }

    @Override
    public PayGrade createPayGrade(PayGrade payGrade) {
        if (payGrade != null) {
            this.existPaygrade(payGrade);
            return payGradeRepository.save(payGrade);
        } else {
            return null;
        }
    }

    @Override
    public List<PayGrade> getAllPayGrades() {
        return payGradeRepository.findAll();
    }

    @Override
    public Page<PayGrade> getAllPayGradesByPage(Integer pageNumber, Integer pageSize, String sortBy,
            String sortDir) {

        Sort sort = null;
        if (sortDir == "asc") {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Page<PayGrade> pagePaygrade = payGradeRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return pagePaygrade;
    }

    @Override
    public Optional<PayGrade> getById(int id) {
        return payGradeRepository.findById(id);
    }

    @Override
    public boolean deletePayGradeById(Integer id) {
        if (id == null) {
            return false;
        }
        Optional<PayGrade> payGradeOptional = payGradeRepository.findById(id);

        if (payGradeOptional.isEmpty()) {
            return false;
        } else {
            PayGrade existPaygrade = payGradeOptional.get();
            if (existPaygrade.getEmployee().size() > 0) {
                throw new EmptyInputException("Paygrade cannot be deleted. There are employees associated,");
            } else {
                if (existPaygrade != null)
                    payGradeRepository.delete(existPaygrade);
                return true;
            }
        }

    }

    // working same to cascade but here adding both manually

    @Override
    public PayGrade add_combine(PayGrade p) {
        if (p == null) {
            throw new EmptyInputException("Paygrade");
        }
        PayGrade p1 = payGradeRepository.save(p);
        List<Employee> e = p1.getEmployee();
        for (Employee e2 : e) {
            e2.setPaygrade(p1);
            employeeRepository.save(e2);

        }
        return p1;
    }

    PayGrade updateDetails(PayGrade existingPayGrade, PayGrade updatedPayGrade) {
        if (updatedPayGrade.getPaygradeName() != null && !updatedPayGrade.getPaygradeName().isEmpty()) {
            this.existPaygrade(updatedPayGrade);
            existingPayGrade.setPaygradeName(updatedPayGrade.getPaygradeName());
        }
        if (updatedPayGrade.getMinPay() != null && updatedPayGrade.getMinPay() != 0) {
            existingPayGrade.setMinPay(updatedPayGrade.getMinPay());
        }
        if (updatedPayGrade.getHra() != null && updatedPayGrade.getHra() != 0) {
            existingPayGrade.setHra(updatedPayGrade.getHra());
        }

        if (updatedPayGrade.getPfs() != null && updatedPayGrade.getPfs() != 0) {
            existingPayGrade.setPfs(updatedPayGrade.getPfs());
        }

        if (updatedPayGrade.getDa() != null && updatedPayGrade.getDa() != 0) {
            existingPayGrade.setDa(updatedPayGrade.getDa());
        }

        if (updatedPayGrade.getMaxPay() != null && updatedPayGrade.getMaxPay() != 0) {
            existingPayGrade.setMaxPay(updatedPayGrade.getMaxPay());
        }
        return existingPayGrade;
    }

    @Override
    public PayGrade updateById(Integer id, PayGrade updatedPayGrade) {
        if (id == null) {
            return null;
        } else {
            PayGrade existingPayGrade = payGradeRepository.findById(id).orElse(null);
            if (existingPayGrade != null) {

                existingPayGrade = updateDetails(existingPayGrade, updatedPayGrade);
                if (existingPayGrade != null)
                    return payGradeRepository.save(existingPayGrade);
                else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    @Override
    public Page<PayGrade> searchPayGradeByName(Integer pageNumber, Integer pageSize, String paygradeName, String sortBy,
            String sortDir) {
        Sort sort = null;
        if (sortDir.equals("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }
        PageRequest p = PageRequest.of(pageNumber, pageSize, sort);
        return payGradeRepository.findByPaygradeNameStartsWithIgnoreCase(p, paygradeName);
    }

    @Override
    public List<Employee> filterByPaygrade(String paygradeName) {
        PayGrade existPaygrade = payGradeRepository.findByPaygradeName(paygradeName);
        return existPaygrade.getEmployee();
    }

    @Override
    public PayGrade getPaygrade(String name) {
        return payGradeRepository.findByPaygradeName(name);
    }
}

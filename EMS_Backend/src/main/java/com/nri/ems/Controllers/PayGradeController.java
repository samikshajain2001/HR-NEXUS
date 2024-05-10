package com.nri.ems.Controllers;

import java.util.List;
import java.util.Optional;

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

import com.nri.ems.Entities.Employee;
import com.nri.ems.Entities.PayGrade;
import com.nri.ems.Services.PayGradeService;

@RestController
@CrossOrigin
@RequestMapping("/paygrade")
public class PayGradeController {
    @Autowired
    PayGradeService payGradeService;

    @GetMapping("/get-all")
    public List<PayGrade> getAllPayGrades() {
        return payGradeService.getAllPayGrades();
    }

    @GetMapping("/page-all")
    public Page<PayGrade> getAllPayGrades(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        return payGradeService.getAllPayGradesByPage(pageNumber, pageSize, sortBy, sortDir);
    }

    @PostMapping("/create")
    public PayGrade createPayGrade(@RequestBody PayGrade payGrade) {
        return payGradeService.createPayGrade(payGrade);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PayGrade> getPayGradeById(@PathVariable("id") int id) {
        Optional<PayGrade> payGrade = payGradeService.getById(id);
        if (!payGrade.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(payGrade.get(), HttpStatus.OK);
        }
    }

    @DeleteMapping("/del-id/{id}")
    public ResponseEntity<PayGrade> deletePayGradeById(@PathVariable("id") Integer id) {
        Boolean payGrade = payGradeService.deletePayGradeById(id);
        if (!payGrade) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PutMapping("/upd-id/{id}")
    public ResponseEntity<PayGrade> updatePayGradeById(@PathVariable("id") Integer id,
            @RequestBody PayGrade updatePayGrade) {
        PayGrade payGrade = payGradeService.updateById(id, updatePayGrade);
        if (payGrade == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(payGrade, HttpStatus.OK);
        }
    }

    @GetMapping("/search-name")
    public Page<PayGrade> searchPaygrade(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "paygradeName", defaultValue = "", required = false) String paygradeName,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        return payGradeService.searchPayGradeByName(pageNumber, pageSize, paygradeName, sortBy, sortDir);
    }

    @PostMapping("/adding/combine")
    public ResponseEntity<PayGrade> adding_combine(@RequestBody PayGrade p) {

        return new ResponseEntity<>(payGradeService.add_combine(p), HttpStatus.OK);
    }

    @GetMapping("/get-Employees")
    public List<Employee> getAllEmployees(@RequestParam String name) {
        return payGradeService.filterByPaygrade(name);
    }

}

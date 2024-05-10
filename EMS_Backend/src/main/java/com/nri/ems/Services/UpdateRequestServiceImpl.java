package com.nri.ems.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nri.ems.DTO.RequestDTO;
import com.nri.ems.Entities.Employee;
import com.nri.ems.Entities.UpdateRequest;
import com.nri.ems.Exceptions.ResourceNotFoundException;
import com.nri.ems.Repositories.RequestRepository;
import com.nri.ems.Utils.Enums.RequestStatus;

import jakarta.mail.MessagingException;

@Service
public class UpdateRequestServiceImpl implements UpdateRequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    MailService mailService;

    public UpdateRequest getRequest(RequestDTO reqBody) {
        UpdateRequest request = UpdateRequest.builder()
                .requestedBy(reqBody.getRequestedBy())
                .build();
        if (!reqBody.getEmployee().getFirstName().isEmpty()) {
            request.setFirstName(reqBody.getEmployee().getFirstName());
        }
        if (reqBody.getEmployee().getProfileImgUrl() != null) {
            request.setProfileImgUrl(reqBody.getEmployee().getProfileImgUrl());
        }
        if (!reqBody.getEmployee().getLastName().isEmpty()) {
            request.setLastName(reqBody.getEmployee().getLastName());
        }
        if (!reqBody.getEmployee().getCity().isEmpty()) {
            request.setCity(reqBody.getEmployee().getCity());
        }
        if (!reqBody.getEmployee().getMobileNo().isEmpty()) {
            request.setMobileNo(reqBody.getEmployee().getMobileNo());
        }
        if (reqBody.getEmployee().getDob() != null) {
            request.setDob(reqBody.getEmployee().getDob());
        }
        if (!reqBody.getEmployee().getPersonalEmail().isEmpty()) {
            request.setPersonalEmail(reqBody.getEmployee().getPersonalEmail());
        }
        if (!reqBody.getEmployee().getStreet().isEmpty()) {
            request.setStreet(reqBody.getEmployee().getStreet());
        }
        if (!reqBody.getEmployee().getCity().isEmpty()) {
            request.setCity(reqBody.getEmployee().getCity());
        }
        if (!reqBody.getEmployee().getPincode().isEmpty()) {
            request.setPincode(reqBody.getEmployee().getPincode());
        }
        if (!reqBody.getEmployee().getState().isEmpty()) {
            request.setState(reqBody.getEmployee().getState());
        }

        if (reqBody.getEmployee().getGender() != null) {
            request.setGender(reqBody.getEmployee().getGender());
        }
        return request;
    }

    @Override
    public UpdateRequest createRequest(RequestDTO reqBody) {
        UpdateRequest request = this.getRequest(reqBody);
        if (request == null) {
            throw new ResourceNotFoundException("Request", null, null);
        }
        Employee existEmployee = employeeService.getByUsername(request.getRequestedBy());
        if (existEmployee == null) {
            throw new ResourceNotFoundException("Employee", null, null);
        } else {
            if (this.checkUpdateRequest(reqBody.getRequestedBy()) != null) {
                return null;
            } else {
                request.setStatus(RequestStatus.PENDING);
                return requestRepository.save(request);
            }
        }
    }

    @Override
    public Page<UpdateRequest> getAllRequest(Integer pageNo, Integer pageSize) {
        Pageable p = PageRequest.of(pageNo, pageSize);
        Page<UpdateRequest> pageEmployee = this.requestRepository.findAll(p);
        return pageEmployee;
    }

    private Employee setEmployee(Employee request, UpdateRequest reqBody) {
        if (reqBody.getFirstName() != null) {
            request.setFirstName(reqBody.getFirstName());
        }
        if (reqBody.getLastName() != null) {
            request.setLastName(reqBody.getLastName());
        }
        if (reqBody.getCity() != null) {
            request.setCity(reqBody.getCity());
        }
        if (reqBody.getProfileImgUrl() != null) {
            request.setProfileImgUrl(reqBody.getProfileImgUrl());
        }
        if (reqBody.getMobileNo() != null) {
            request.setMobileNo(reqBody.getMobileNo());
        }
        if (reqBody.getDob() != null) {
            request.setDob(reqBody.getDob());
        }
        if (reqBody.getCompanyEmail() != null) {
            request.setCompanyEmail(reqBody.getCompanyEmail());
        }
        if (reqBody.getPersonalEmail() != null) {
            request.setPersonalEmail(reqBody.getPersonalEmail());
        }
        if (reqBody.getStreet() != null) {
            request.setStreet(reqBody.getStreet());
        }
        if (reqBody.getCity() != null) {
            request.setCity(reqBody.getCity());
        }
        if (reqBody.getPincode() != null) {
            request.setPincode(reqBody.getPincode());
        }
        if (reqBody.getState() != null) {
            request.setState(reqBody.getState());
        }

        if (reqBody.getGender() != null) {
            request.setGender(reqBody.getGender());
        }
        return request;

    }

    @Override
    public UpdateRequest updateStatus(RequestStatus status, Integer id, String role, String hrUsername)
            throws MessagingException {
        if (id == null) {
            throw new ResourceNotFoundException("id", null, 0);
        }
        UpdateRequest currenRequest = requestRepository.findById(id).orElse(null);
        if (currenRequest == null || role == "ROLE_EMPLOYEE" || currenRequest.getStatus().name().equals("APPROVED")) {
            throw new ResourceNotFoundException("id", null, 0);
        }
        currenRequest.setStatus(status);
        currenRequest.setUpdatedBy(hrUsername);
        Employee existEmployee = employeeService.getByUsername(currenRequest.getRequestedBy());
        if (existEmployee == null) {
            throw new ResourceNotFoundException("Employee", null, 0);
        }
        if (status.name().equals("APPROVED")) {

            existEmployee = this.setEmployee(existEmployee, currenRequest);
            employeeService.updateDetails(existEmployee);
            requestRepository.save(currenRequest);
            Employee employee = employeeService.getByUsername(currenRequest.getRequestedBy());
            requestRepository.delete(currenRequest);
            String name = employee.getFirstName() + " " + employee.getLastName();
            mailService.sendApproveMail(employee.getPersonalEmail(), name);
        } else {
            mailService.sendRejectMail(existEmployee.getPersonalEmail(),
                    existEmployee.getFirstName() + " " + existEmployee.getLastName());
            requestRepository.delete(currenRequest);
        }
        return currenRequest;
    }

    @Override
    public UpdateRequest checkUpdateRequest(String requestedBy) {
        UpdateRequest existRequest = requestRepository.findByRequestedBy(requestedBy);
        return existRequest;
    }
}

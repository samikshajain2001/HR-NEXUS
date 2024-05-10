package com.nri.ems.Services;

import org.springframework.data.domain.Page;

import com.nri.ems.DTO.RequestDTO;
import com.nri.ems.Entities.UpdateRequest;
import com.nri.ems.Utils.Enums.RequestStatus;

import jakarta.mail.MessagingException;

public interface UpdateRequestService {
    UpdateRequest createRequest(RequestDTO reqBody);

    Page<UpdateRequest> getAllRequest(Integer pageNo, Integer pageSize);

    UpdateRequest updateStatus(RequestStatus status, Integer id, String role, String hrUsername)
            throws MessagingException;

    UpdateRequest checkUpdateRequest(String requestedBy);
}

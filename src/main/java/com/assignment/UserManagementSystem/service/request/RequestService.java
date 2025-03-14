package com.assignment.UserManagementSystem.service.request;

import com.assignment.UserManagementSystem.dto.RequestDto;
import com.assignment.UserManagementSystem.request.AccountRegistrationRequest;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(AccountRegistrationRequest request);

    String checkRequestStatus(String nrcNumber);

    RequestDto approveRequest(Long id);

    RequestDto rejectRequest(Long id);

    RequestDto blockRequest(Long id);

    RequestDto unblockRequest(Long id);

    List<RequestDto> getAllRequests();

    RequestDto getARequest(Long id);

}

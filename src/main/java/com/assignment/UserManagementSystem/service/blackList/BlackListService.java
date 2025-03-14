package com.assignment.UserManagementSystem.service.blackList;

import com.assignment.UserManagementSystem.dto.BlackListDto;

public interface BlackListService {

    BlackListDto blackListARequest(Long requestId);

    void deleteBlackList(Long id);

}

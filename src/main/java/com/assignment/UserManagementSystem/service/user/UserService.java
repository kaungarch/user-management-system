package com.assignment.UserManagementSystem.service.user;

import com.assignment.UserManagementSystem.dto.UserDto;
import com.assignment.UserManagementSystem.dto.UserSearchCriteria;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserDto> getAllUsers(UserSearchCriteria criteria);

    UserDto getAUser(UUID id);

}

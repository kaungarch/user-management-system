package com.assignment.UserManagementSystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearchCriteria {

    private String name;
    private String nrcNumber;
    private String phoneNumber;
    private String role;

}

package com.assignment.UserManagementSystem.dto;

import com.assignment.UserManagementSystem.model.enums.UserRole;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private UUID id;

    private String name;

    private String nrcNumber;

    private String phoneNumber;

    private UserRole role;

}

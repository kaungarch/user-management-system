package com.assignment.UserManagementSystem.dto;

import com.assignment.UserManagementSystem.model.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RequestDto {

    private Long id;

    private String name;

    private String phoneNumber;

    private String nrcNumber;

    private RequestStatus status;

}

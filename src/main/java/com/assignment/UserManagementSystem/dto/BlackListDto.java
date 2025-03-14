package com.assignment.UserManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackListDto {

    private Long id;

    private String nrcNumber;

    private String phoneNumber;

}

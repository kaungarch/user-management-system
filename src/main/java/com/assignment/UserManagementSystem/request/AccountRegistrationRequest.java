package com.assignment.UserManagementSystem.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AccountRegistrationRequest(
        @NotBlank(message = "name is required.")
        String name,
        @NotBlank(message = "phone number is required.")
        @Min(9)
        String phoneNumber,
        @NotBlank(message = "nrc number is required")
        String nrcNumber,
        @NotBlank
        @Min(6)
        String password
) {
}

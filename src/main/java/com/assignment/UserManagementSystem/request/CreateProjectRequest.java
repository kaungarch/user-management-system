package com.assignment.UserManagementSystem.request;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectRequest(
        @NotBlank
        String title,
        @NotBlank
        String description
) {
}

package com.assignment.UserManagementSystem.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProjectRequest(

        String title,

        String description
) {
}

package com.assignment.UserManagementSystem.request;

public record AuthRequest(
        String phoneNumber,
        String password
) {
}

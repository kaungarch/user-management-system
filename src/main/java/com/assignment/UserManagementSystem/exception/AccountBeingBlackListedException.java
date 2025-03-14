package com.assignment.UserManagementSystem.exception;

public class AccountBeingBlackListedException extends RuntimeException {
    public AccountBeingBlackListedException(String message
    ) {
        super(message);
    }
}

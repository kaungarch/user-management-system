package com.assignment.UserManagementSystem.service.auth;

import com.assignment.UserManagementSystem.request.AuthRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    UserDetails signIn(AuthRequest request);

    UserDetails refresh(String refreshToken);

}

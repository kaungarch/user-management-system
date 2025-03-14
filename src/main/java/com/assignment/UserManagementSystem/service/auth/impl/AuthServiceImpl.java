package com.assignment.UserManagementSystem.service.auth.impl;

import com.assignment.UserManagementSystem.exception.AuthenticationException;
import com.assignment.UserManagementSystem.exception.ResourceNotFoundException;
import com.assignment.UserManagementSystem.model.Request;
import com.assignment.UserManagementSystem.model.User;
import com.assignment.UserManagementSystem.model.enums.UserRole;
import com.assignment.UserManagementSystem.repository.UserRepository;
import com.assignment.UserManagementSystem.request.AuthRequest;
import com.assignment.UserManagementSystem.security.jwt.JwtService;
import com.assignment.UserManagementSystem.service.auth.AuthService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails signIn(AuthRequest request) throws BadCredentialsException, AuthenticationException {

        User user = userRepository.findByPhoneNumber(request.phoneNumber()).orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getId(),
                request.password()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return (UserDetails) authentication.getPrincipal();

    }

    @Override
    public UserDetails refresh(String refreshToken) {

        if (refreshToken == null) throw new AuthenticationException("Refresh token is missing");

        boolean isTokenValid = jwtService.isTokenValid(refreshToken);

        if (!isTokenValid)
            throw new JwtException("Invalid or expired refresh token. Please log in again.");

        String nrcNumber = jwtService.extractUserId(refreshToken);
        Optional<User> user = userRepository.findByPhoneNumber(nrcNumber);
        if (user.isPresent()) return user.get();
        else throw new ResourceNotFoundException("User not found.");
    }


    private User buildUserFromRequest(AuthRequest request, Request foundRequest) {
        return User.builder()
                .id(null)
                .name(foundRequest.getName())
                .nrcNumber(foundRequest.getNrcNumber())
                .phoneNumber(foundRequest.getPhoneNumber())
                .role(UserRole.USER)
                .projects(new ArrayList<>())
                .password(passwordEncoder.encode(request.password()))
                .build();
    }
}

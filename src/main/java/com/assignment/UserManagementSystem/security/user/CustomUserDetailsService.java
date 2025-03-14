package com.assignment.UserManagementSystem.security.user;

import com.assignment.UserManagementSystem.exception.ResourceNotFoundException;
import com.assignment.UserManagementSystem.model.User;
import com.assignment.UserManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepo.findById(UUID.fromString(userId)).orElseThrow(() -> new UsernameNotFoundException("User not found.")
    );
}
}

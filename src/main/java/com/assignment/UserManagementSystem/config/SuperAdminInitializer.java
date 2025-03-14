package com.assignment.UserManagementSystem.config;

import com.assignment.UserManagementSystem.model.User;
import com.assignment.UserManagementSystem.model.enums.UserRole;
import com.assignment.UserManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SuperAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Optional<User> user = userRepository.findByRole(UserRole.SUPER_ADMIN);

        User superAdmin = User.builder()
                .id(null)
                .name("kaung")
                .nrcNumber("10/thaphaya(N)123456")
                .password(passwordEncoder.encode("123456"))
                .projects(new LinkedList<>())
                .phoneNumber("959973468287")
                .role(UserRole.SUPER_ADMIN)
                .build();


        user.ifPresentOrElse(
            existingUser -> {},
            () -> userRepository.save(superAdmin)
        );
    }
}

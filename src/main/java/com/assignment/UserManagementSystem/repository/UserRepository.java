package com.assignment.UserManagementSystem.repository;

import com.assignment.UserManagementSystem.model.User;
import com.assignment.UserManagementSystem.model.enums.UserRole;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByNrcNumber(String nrcNumber);

    Optional<User> findByRole(UserRole userRole);

    Optional<User> findByPhoneNumber(String phoneNumber);
}

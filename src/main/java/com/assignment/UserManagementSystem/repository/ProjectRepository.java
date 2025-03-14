package com.assignment.UserManagementSystem.repository;

import com.assignment.UserManagementSystem.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByIdAndUserId(Long id, UUID userId);
}

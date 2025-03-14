package com.assignment.UserManagementSystem.repository;

import com.assignment.UserManagementSystem.model.Request;
import com.assignment.UserManagementSystem.model.enums.RequestStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findByNrcNumberAndStatus(@NotBlank(message = "nrc number is required") String s, RequestStatus requestStatus);

    Optional<Request> findByPhoneNumber(String phoneNumber);
}

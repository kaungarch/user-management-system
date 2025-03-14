package com.assignment.UserManagementSystem.repository;

import com.assignment.UserManagementSystem.model.BlackList;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlackListRepository extends JpaRepository<BlackList, Long>{
    Optional<BlackList> findByNrcNumber(String nrcNumber);

    Optional<BlackList> findByPhoneNumber(String phoneNumber);
}

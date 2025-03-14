package com.assignment.UserManagementSystem.model;

import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class BaseEntity {

    private LocalDate createdAt;

    private LocalDate updatedAt;

}

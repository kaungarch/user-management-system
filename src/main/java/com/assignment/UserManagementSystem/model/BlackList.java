package com.assignment.UserManagementSystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String nrcNumber;

    private LocalDateTime blackListedAt;

    @PrePersist
    public void onCreate() {
        setBlackListedAt(LocalDateTime.now());
    }

}

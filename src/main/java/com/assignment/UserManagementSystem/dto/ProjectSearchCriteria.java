package com.assignment.UserManagementSystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectSearchCriteria {

    private String title;
    private Boolean deleted = null;
    private LocalDate createdAfter;
    private LocalDate createdBefore;

}

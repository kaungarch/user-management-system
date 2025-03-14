package com.assignment.UserManagementSystem.service.project;

import com.assignment.UserManagementSystem.dto.ProjectDto;
import com.assignment.UserManagementSystem.dto.ProjectSearchCriteria;
import com.assignment.UserManagementSystem.request.CreateProjectRequest;
import com.assignment.UserManagementSystem.request.UpdateProjectRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectService {

    ProjectDto createProject(CreateProjectRequest request, String authorizationHeader);

    ProjectDto updateProject(UpdateProjectRequest request, Long id, String authHeader);

    List<ProjectDto> getAllProjects(String authHeader, ProjectSearchCriteria criteria);

    void softDeleteProject(String authHeader, Long projectId);

    void undeleteProject(String authHeader, Long projectId);

    void deleteProject(Long id, String authHeader);

}

package com.assignment.UserManagementSystem.service.project.impl;

import com.assignment.UserManagementSystem.dto.ProjectDto;
import com.assignment.UserManagementSystem.dto.ProjectSearchCriteria;
import com.assignment.UserManagementSystem.exception.ResourceNotFoundException;
import com.assignment.UserManagementSystem.model.Project;
import com.assignment.UserManagementSystem.model.User;
import com.assignment.UserManagementSystem.model.enums.UserRole;
import com.assignment.UserManagementSystem.repository.ProjectCriteriaRepository;
import com.assignment.UserManagementSystem.repository.ProjectRepository;
import com.assignment.UserManagementSystem.repository.UserRepository;
import com.assignment.UserManagementSystem.request.CreateProjectRequest;
import com.assignment.UserManagementSystem.request.UpdateProjectRequest;
import com.assignment.UserManagementSystem.security.jwt.JwtService;
import com.assignment.UserManagementSystem.service.project.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectCriteriaRepository projectCriteriaRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @Override
    public ProjectDto createProject(CreateProjectRequest request, String authorizationHeader) {

        User user = getUserUsingAuthHeader(authorizationHeader);

        LocalDateTime dateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Project newProject = Project.builder()
                .id(null)
                .title(request.title())
                .description(request.description())
                .deleted(false)
                .user(user)
                .build();

        Project savedProject = projectRepository.save(newProject);
        return modelMapper.map(savedProject, ProjectDto.class);
    }

    @Override
    public ProjectDto updateProject(UpdateProjectRequest request, Long id, String authHeader) {

        User user = getUserUsingAuthHeader(authHeader);

        Optional<Project> foundProject = projectRepository.findByIdAndUserId(id, user.getId());

        if (foundProject.isEmpty()) throw new ResourceNotFoundException("Project not found.");

        Project project = foundProject.get();

        if (project.isDeleted()) throw new ResourceNotFoundException("Project is deleted.");

        Optional.ofNullable(request.title()).ifPresent(project::setTitle);
        Optional.ofNullable(request.description()).ifPresent(project::setDescription);

        Project updated = projectRepository.save(project);

        return modelMapper.map(updated, ProjectDto.class);
    }

    @Override
    public List<ProjectDto> getAllProjects(String authHeader, ProjectSearchCriteria criteria) {
        User user = getUserUsingAuthHeader(authHeader);
        String userId = user.getId().toString();

        if (user.getRole().equals(UserRole.SUPER_ADMIN)) {
            return projectCriteriaRepository.searchProjectsByAdmin(criteria)
                    .stream().map(project -> modelMapper.map(project, ProjectDto.class))
                    .toList();
        } else {
            return projectCriteriaRepository.searchProjects(criteria, userId).stream()
                    .map(project -> modelMapper.map(project, ProjectDto.class))
                    .toList();
        }
    }

    @Override
    public void softDeleteProject(String authHeader, Long projectId) {
        User user = getUserUsingAuthHeader(authHeader);
        Project project = projectRepository.findByIdAndUserId(projectId, user.getId()).orElseThrow(() -> new ResourceNotFoundException(
                "Project not found."));
        project.setDeleted(true);
        projectRepository.save(project);

    }

    @Override
    public void undeleteProject(String authHeader, Long projectId) {
        User user = getUserUsingAuthHeader(authHeader);
        Project project =
                projectRepository.findByIdAndUserId(projectId, user.getId()).orElseThrow(() -> new ResourceNotFoundException(
                        "Project not found."));
        project.setDeleted(false);
        projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id, String authHeader) {
        User user = getUserUsingAuthHeader(authHeader);
        Project project = projectRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new ResourceNotFoundException(
                "Project not found."));

        projectRepository.delete(project);
    }

    private User getUserUsingAuthHeader(String authHeader) {
        String accessToken = authHeader.substring(7);
        String userId = jwtService.extractUserId(accessToken);
        return userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> {
            throw new ResourceNotFoundException("User not " +
                    "found with id " + userId);
        });
    }
}

package com.assignment.UserManagementSystem.controller.project;

import com.assignment.UserManagementSystem.dto.ProjectDto;
import com.assignment.UserManagementSystem.dto.ProjectSearchCriteria;
import com.assignment.UserManagementSystem.exception.ResourceNotFoundException;
import com.assignment.UserManagementSystem.request.CreateProjectRequest;
import com.assignment.UserManagementSystem.request.UpdateProjectRequest;
import com.assignment.UserManagementSystem.response.BaseResponse;
import com.assignment.UserManagementSystem.service.project.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(name = "Project Controller", description = "Endpoints for managing projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(
            summary = "Create a project (Only authenticated user can access)"
    )
    public ResponseEntity<BaseResponse> createProject(@RequestBody @Valid CreateProjectRequest request,
                                                      @RequestHeader(value = "Authorization") String authHeader,
                                                      BindingResult result) {
        if (result.hasFieldErrors()) {
            BaseResponse baseResponse = new BaseResponse(null, "Bad request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        try {
            ProjectDto projectDto = projectService.createProject(request, authHeader);
            BaseResponse baseResponse = new BaseResponse(projectDto, "your request is successful.");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } catch (ResourceNotFoundException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update existing project (Only authenticated user can access)"
    )
    public ResponseEntity<BaseResponse> updateProject(@RequestBody @Valid UpdateProjectRequest request,
                                                      @PathVariable Long id,
                                                      @RequestHeader(value = "Authorization") String authHeader) {
        try {
            ProjectDto projectDto = projectService.updateProject(request, id, authHeader);
            BaseResponse baseResponse = new BaseResponse(projectDto, "your request is successful.");
            return ResponseEntity.ok(baseResponse);
        } catch (ResourceNotFoundException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping
    @Operation(
            summary = "Get all projects (Both admin and authenticated user can access)"
    )
    public ResponseEntity<BaseResponse> getAllProjects(@RequestHeader(value = "Authorization") String authHeader,
                                                       @ModelAttribute ProjectSearchCriteria criteria
    ) {
        try {
            List<ProjectDto> projectDtoList = projectService.getAllProjects(authHeader, criteria);
            BaseResponse baseResponse = new BaseResponse(projectDtoList, "your request is successful.");
            return ResponseEntity.ok(baseResponse);
        } catch (ResourceNotFoundException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping("/{id}/soft-delete")
    @Operation(
            summary = "Soft delete existing project (Only authenticated user can access)"
    )
    public ResponseEntity<BaseResponse> softDeleteProject(
            @RequestHeader(name = "Authorization") String authHeader,
            @PathVariable(name = "id") Long projectId
    ) {
        try {
            projectService.softDeleteProject(authHeader, projectId);
            BaseResponse baseResponse = new BaseResponse(null, "your request is successful.");
            return ResponseEntity.ok(baseResponse);
        } catch (ResourceNotFoundException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping("/{id}/undelete")
    @Operation(
            summary = "Undo project deletion (Only authenticated user can access)"
    )
    public ResponseEntity<BaseResponse> undeleteProject(
            @RequestHeader(name = "Authorization") String authHeader,
            @PathVariable(name = "id") Long projectId
    ) {
        try {
            projectService.undeleteProject(authHeader, projectId);
            BaseResponse baseResponse = new BaseResponse(null, "your request is successful.");
            return ResponseEntity.ok(baseResponse);
        } catch (ResourceNotFoundException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete project permanently (Only authenticated user can access)"
    )
    public ResponseEntity<BaseResponse> deleteProject(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable Long id
    ) {
        try {
            projectService.deleteProject(id, authHeader);
            BaseResponse baseResponse = new BaseResponse(null, "your request is successful.");
            return ResponseEntity.ok(baseResponse);
        } catch (ResourceNotFoundException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

}

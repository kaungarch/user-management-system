package com.assignment.UserManagementSystem.controller.user;

import com.assignment.UserManagementSystem.dto.UserDto;
import com.assignment.UserManagementSystem.dto.UserSearchCriteria;
import com.assignment.UserManagementSystem.exception.ResourceNotFoundException;
import com.assignment.UserManagementSystem.response.BaseResponse;
import com.assignment.UserManagementSystem.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(
            summary = "Get all users (Only admin can access)"
    )
    public ResponseEntity<BaseResponse> getAllUsers(
           @ModelAttribute UserSearchCriteria criteria
    ) {
        List<UserDto> userDtoList = userService.getAllUsers(criteria);
        BaseResponse baseResponse = new BaseResponse(userDtoList, "your request is successful.");
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID (Only admin can access)"
    )
    public ResponseEntity<BaseResponse> getAUser(@PathVariable String id) {
        try {
            UserDto userDto = userService.getAUser(UUID.fromString(id));
            BaseResponse baseResponse = new BaseResponse(userDto, "your request is successful.");
            return ResponseEntity.ok(baseResponse);
        } catch (IllegalArgumentException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } catch (ResourceNotFoundException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

}

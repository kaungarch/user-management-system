package com.assignment.UserManagementSystem.controller.request;

import com.assignment.UserManagementSystem.dto.RequestDto;
import com.assignment.UserManagementSystem.exception.AlreadyExistedException;
import com.assignment.UserManagementSystem.exception.BadRequestException;
import com.assignment.UserManagementSystem.exception.RequestCreationException;
import com.assignment.UserManagementSystem.exception.ResourceNotFoundException;
import com.assignment.UserManagementSystem.request.AccountRegistrationRequest;
import com.assignment.UserManagementSystem.response.BaseResponse;
import com.assignment.UserManagementSystem.service.request.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
@Tag(name = "Request Controller", description = "Endpoints for managing requests")
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    @Operation(
            summary = "Create a request (Only guest user can access)"
    )
    public ResponseEntity<BaseResponse> createRequest(@RequestBody @Valid AccountRegistrationRequest request,
                                                      BindingResult result) {
        try {
            if (result.hasFieldErrors()) {
                return ResponseEntity.badRequest().body(new BaseResponse(null, "Bad request"));
            }
            RequestDto requestDto = requestService.createRequest(request);
            BaseResponse baseResponse = new BaseResponse(requestDto, "your request is successful.");
            return ResponseEntity.ok(baseResponse);
        } catch (RequestCreationException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(baseResponse);
        } catch (AlreadyExistedException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping("/status")
    @Operation(
            summary = "Check request status using phone number (Anyone can access)"
    )
    public ResponseEntity<BaseResponse> checkRequestStatus(@RequestParam @NotBlank String phoneNumber) {
        try {
            String status = requestService.checkRequestStatus(phoneNumber);
            BaseResponse baseResponse = new BaseResponse(status, "your request is successful.");
            return ResponseEntity.ok(baseResponse);
        } catch (ResourceNotFoundException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping("/{id}/approve")
    @Operation(
            summary = "Approve a request (Only admin can access)"
    )
    public ResponseEntity<BaseResponse> approveRequest(@PathVariable Long id) {
        try {
            RequestDto requestDto = requestService.approveRequest(id);
            BaseResponse baseResponse = new BaseResponse(requestDto, "your request is successful.");
            return ResponseEntity.ok(baseResponse);
        } catch (ResourceNotFoundException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (BadRequestException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping("/{id}/reject")
    @Operation(
            summary = "Reject a request (Only admin can access)"
    )
    public ResponseEntity<BaseResponse> rejectRequest(@PathVariable Long id) {
        try {
            RequestDto requestDto = requestService.rejectRequest(id);
            BaseResponse baseResponse = new BaseResponse(requestDto, "your request is successful.");
            return ResponseEntity.ok(baseResponse);
        } catch (ResourceNotFoundException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (BadRequestException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping("/{id}/block")
    @Operation(
            summary = "Block a request (Only admin can access)"
    )
    public ResponseEntity<BaseResponse> blockRequest(@PathVariable Long id) {
        try {
            RequestDto requestDto = requestService.blockRequest(id);
            BaseResponse baseResponse = new BaseResponse(requestDto, "your request is successful.");
            return ResponseEntity.ok(baseResponse);
        } catch (ResourceNotFoundException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (BadRequestException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping("/{id}/unblock")
    @Operation(
            summary = "Unblock a request (Only admin can access)"
    )
    public ResponseEntity<BaseResponse> unblockRequest(@PathVariable Long id) {
        try {
            RequestDto requestDto = requestService.unblockRequest(id);
            BaseResponse baseResponse = new BaseResponse(requestDto, "your request is successful.");
            return ResponseEntity.ok(baseResponse);
        } catch (ResourceNotFoundException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (IllegalStateException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping
    @Operation(
            summary = "Get all requests (Only admin can access)"
    )
    public ResponseEntity<BaseResponse> getAllRequests() {
        try {
            List<RequestDto> requestDtoList = requestService.getAllRequests();
            BaseResponse baseResponse = new BaseResponse(requestDtoList, "your request is successful.");
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a request (Only admin can access)"
    )
    public ResponseEntity<BaseResponse> getARequest(@PathVariable Long id) {
        try {
            RequestDto requestDto = requestService.getARequest(id);
            BaseResponse baseResponse = new BaseResponse(requestDto, "your request is successful.");
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

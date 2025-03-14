package com.assignment.UserManagementSystem.controller.blackList;

import com.assignment.UserManagementSystem.dto.BlackListDto;
import com.assignment.UserManagementSystem.exception.AlreadyExistedException;
import com.assignment.UserManagementSystem.exception.BadRequestException;
import com.assignment.UserManagementSystem.exception.ResourceNotFoundException;
import com.assignment.UserManagementSystem.request.CreateBlackListRequest;
import com.assignment.UserManagementSystem.response.BaseResponse;
import com.assignment.UserManagementSystem.service.blackList.BlackListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blacklists")
@Tag(
        name = "Black List Controller",
        description = "Endpoints for managing black lists"
)
public class BlackListController {

    private final BlackListService blackListService;

    @PostMapping
    @Operation(
            summary = "Black list a request (Only admin can access)"
    )
    public ResponseEntity<BaseResponse> blackListARequest(@RequestBody CreateBlackListRequest request) {
        try {
            BlackListDto blackListDto = blackListService.blackListARequest(request.requestId());
            BaseResponse baseResponse = new BaseResponse(blackListDto, "your request is successful.");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } catch (ResourceNotFoundException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        } catch (BadRequestException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } catch (AlreadyExistedException e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = new BaseResponse(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete existing blacklist (Only admin can access)"
    )
    public ResponseEntity<BaseResponse> deleteBlackList(@PathVariable Long id) {
        try {
            blackListService.deleteBlackList(id);
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

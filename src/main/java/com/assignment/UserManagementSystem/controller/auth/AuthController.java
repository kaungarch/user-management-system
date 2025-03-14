package com.assignment.UserManagementSystem.controller.auth;

import com.assignment.UserManagementSystem.exception.AuthenticationException;
import com.assignment.UserManagementSystem.request.AuthRequest;
import com.assignment.UserManagementSystem.response.AuthResponse;
import com.assignment.UserManagementSystem.response.BaseResponse;
import com.assignment.UserManagementSystem.security.jwt.JwtService;
import com.assignment.UserManagementSystem.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication Controller",
        description = "Endpoints for managing authentication processes"
)
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @PostMapping("/signin")
    @Operation(
            summary = "Sign in to account (Anyone can access)"
    )
    public ResponseEntity<AuthResponse> signIn(@RequestBody AuthRequest request, HttpServletResponse response) {
        try {
            UserDetails userDetails = authService.signIn(request);

            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            ResponseCookie responseCookie = ResponseCookie.from("Refresh_token", refreshToken)
                    .httpOnly(true)
                    .sameSite("Strict")
                    .path("/")
                    .secure(true)
                    .maxAge(Duration.ofDays(3))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
            response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

            AuthResponse authResponse = new AuthResponse("success", "Sign in successful.");

            return ResponseEntity
                    .ok()
                    .body(authResponse);
        } catch (BadCredentialsException e) {
            AuthResponse authResponse = new AuthResponse("Failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResponse);
        } catch (UsernameNotFoundException e) {
            AuthResponse authResponse = new AuthResponse("Failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(authResponse);
        } catch (AuthenticationException e) {
            AuthResponse authResponse = new AuthResponse("Failed", "Wrong phone number or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        } catch (Exception e) {
            AuthResponse authResponse = new AuthResponse("Failed", "Internal server error.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(authResponse);
        }
    }

    @PostMapping("/signout")
    @Operation(
            summary = "Sign out from account (Both admin and authenticated users can access)"
    )
    public ResponseEntity<BaseResponse> signOut(HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from("Refresh_token", null)
                .maxAge(0)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        BaseResponse baseResponse = new BaseResponse(null, "your request is successful.");
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/refresh")
    @Operation(
            summary = "Generate a new access token using refresh token (Anyone can access)"
    )
    public ResponseEntity<AuthResponse> getRefreshToken(@CookieValue(value = "Refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        try {
            UserDetails userDetails = authService.refresh(refreshToken);

            String accessToken = jwtService.generateAccessToken(userDetails);

            response.addHeader("Authorization", "Bearer " + accessToken);

            AuthResponse authResponse = new AuthResponse("success", "Refresh token successfully.");
            return ResponseEntity.ok(authResponse);
        } catch (AuthenticationException e) {
            AuthResponse authResponse = new AuthResponse("Failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResponse);
        } catch (JwtException e) {
            AuthResponse authResponse = new AuthResponse("Failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResponse);
        } catch (Exception e) {
            AuthResponse authResponse = new AuthResponse("Failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(authResponse);
        }
    }

}

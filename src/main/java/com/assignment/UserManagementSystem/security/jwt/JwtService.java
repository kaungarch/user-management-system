package com.assignment.UserManagementSystem.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String extractUserId(String token);

    boolean isTokenValid(String token);

    <T> T extractClaim(String token, Function<Claims, T> resolverFunc);

    <T> T extractClaim(String token, String claimKey, Class<T> claimType);

}

package com.assignment.UserManagementSystem.security.jwt;

import com.assignment.UserManagementSystem.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    @Value("${jwt.accessToken.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refreshToken.expiration}")
    private Long refreshTokenExpiration;

    private final UserRepository userRepository;


    @Override
    public String generateAccessToken(UserDetails userDetails) {
        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", userDetails.getAuthorities());
        return buildToken(userDetails, this.accessTokenExpiration).claims(extraClaims).compact();
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, this.refreshTokenExpiration).compact();
    }

    @Override
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token) throws SignatureException {
        try {
            return extractExpiration(token).after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> resolverFunc) {
        Claims claims = extractAllClaims(token);
        return resolverFunc.apply(claims);
    }

    @Override
    public <T> T extractClaim(String token, String claimKey, Class<T> claimType) {
        Claims claims = extractAllClaims(token);
        Object claimValue = claims.get(claimKey);
        if (claimType.isEnum()) {
            return claimType.cast(Enum.valueOf((Class<Enum>) claimType, claimValue.toString()));
        }
        return claimType.cast(claimValue);
    }

    private JwtBuilder buildToken(UserDetails userDetails, Long expiration) {

        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .issuer(jwtIssuer)
                .signWith(getSigningKey());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] decoded = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(decoded);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}

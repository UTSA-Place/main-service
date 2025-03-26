package com.example.UTSAPlaceBackend.JWT;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.example.UTSAPlaceBackend.models.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Collection;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String JWT_KEY;

    private final int ACCESS_TOKEN_EXPIRATION_MS = 1000 * 60 * 60 * 24 * 7;

    private final int REFRESH_TOKEN_EXPIRATION_MS = 100 * 60 * 60;

    private SecretKey getSecretKey() {
        return new SecretKeySpec(JWT_KEY.getBytes(), "HmacSHA256");
    }

    // Method to create a JWT token
    public String createToken(String username, GrantedAuthority role) {
        Map<String, String> claims = Map.of(
                "username", username
        );
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_MS))
                .signWith(getSecretKey())
                .compact();
    }

    // Method to validate a JWT token
    public Boolean validate(String token) {
        try {
            final String username = extractUsername(token);
            return username != null && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Method to extract the username from a JWT token
    public String extractUsername(String tokenString) {
        return extractClaim(tokenString, Claims::getSubject);
    }

    // Method to extract the expiration date from a JWT token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic method to extract a claim from a JWT token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claim = extractClaims(token);
        return claimsResolver.apply(claim);
    }

    // Method to extract all claims from a JWT token
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Method to check if a JWT token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }


// method to check for revoked tokens
//    public boolean isTokenRevoked(String token) {}


   
}
package com.example.UTSAPlaceBackend.util; // Package declaration

import java.util.Date; // Importing Claims class from JJWT library
import java.util.HashMap; // Importing Jwts class from JJWT library
import java.util.Map; 
import java.util.function.Function; // Importing Date class from java.util package

import javax.crypto.SecretKey; // Importing HashMap class from java.util package

import org.springframework.stereotype.Component; // Importing Map interface from java.util package

import io.jsonwebtoken.Claims; // Importing Function interface from java.util.function package
import io.jsonwebtoken.Jwts; // Importing SecretKey interface from javax.crypto package
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component // Marking this class as a Spring component
public class JWTService { // Declaring the JWTService class
    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Initializing secretKey using JJWT library
    private Map<String, Object> claims = new HashMap<>(); // Creating a claims map

    public JWTService(Map<String, Object> claims) { // Constructor with claims parameter
        this.claims = claims; // Assigning the claims parameter to the class variable
     // Constructor
    }


    // Method to create a JWT token
    public String createToken(String username , String role) {
        claims = new HashMap<>(); // Creating an empty claims map
        claims.put(username, role); // Adding the username and role to the claims map
        return Jwts
                .builder() // Building the JWT token
                .setClaims(claims) // Setting the claims
                .setSubject(username) // Setting the subject (username)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Setting the issued date
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Setting the expiration date (1 week)
                .signWith(secretKey) // Signing the token with the secret key
                .compact(); // Compacting the token into a string
    }

    // Method to validate a JWT token
    public Boolean validate(String token, String username) {
        final String dbUsername = extractUsername(token); // Extracting the username from the token
        return username.equals(dbUsername) && !isTokenExpired(token); // Checking if the username matches and the token is not expired
    }

    // Method to extract the username from a JWT token
    public String extractUsername(String tokenString) {
        return extractClaim(tokenString, Claims::getSubject); // Extracting the subject (username) from the token
    }

    // Method to extract the expiration date from a JWT token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Extracting the expiration date from the token
    }

    // Generic method to extract a claim from a JWT token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claim = extractClaims(token); // Extracting all claims from the token
        return claimsResolver.apply(claim); // Applying the claims resolver function to the claims
    }

    // Method to extract all claims from a JWT token
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()// Creating a JWT parser
            .setSigningKey(secretKey) // Setting the secret key for verification
            .build() // Building the parser
            .parseClaimsJws(token) // Parsing the claims from the token
            .getBody(); // Getting the body (claims) from the parsed token
    }

    // Method to check if a JWT token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Checking if the expiration date is before the current date
    }

    // Static method to parse a JWT token and extract claims
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()// Creating a JWT parser
                .setSigningKey(secretKey) // Setting the secret key for verification
                .build() // Building the parser
                .parseClaimsJws(token) // Parsing the signed claims from the token
                .getBody(); // Getting the body (claims) from the parsed token
    }



   
}
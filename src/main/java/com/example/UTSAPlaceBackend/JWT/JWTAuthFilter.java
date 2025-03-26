package com.example.UTSAPlaceBackend.JWT;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;


@Slf4j
@Component
@AllArgsConstructor
public class JWTAuthFilter  extends OncePerRequestFilter {

    private final JWTService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {


        log.info("Performing JWT authentication: {}", String.format("%s %s by address %s", request.getMethod(), request.getRequestURI(), request.getRemoteAddr()));
        String authHeader = request.getHeader("Authorization");

        // If auth header is missing or token is not preceded with "Bearer" fail auth
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("Missing JWT or malformed token");
            // Must always continue filter chain
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        // If token is valid username is present in token and no user is currently authenticated
        if (jwtService.validate(token) && username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        // Get user from DB
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // Set user in spring auth context with roles/permissions
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            log.info("JWT authentication Failed. Token or username invalid: {}", token);
        }


    // Must always continue filter chain
    filterChain.doFilter(request, response);

    }
}
    
    
      

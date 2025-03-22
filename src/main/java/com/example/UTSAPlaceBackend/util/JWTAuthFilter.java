package com.example.UTSAPlaceBackend.util;

import java.io.IOException;

import com.example.UTSAPlaceBackend.config.RouterValidator;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    private final RouterValidator routerValidator;

    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (routerValidator.isSecured.test(request)) {
            log.info("JWT Authentication filter request: {}", String.format("%s %s", request.getMethod(), request.getRequestURI()));
            String token = request.getHeader("Authorization");

            if (token == null || !token.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // TODO: Respond by redirecting to login
                response.getWriter().write("Unauthorized request.");
                log.info("Missing JWT token");
                return;
            }
            token = token.substring(7);
            try {
                boolean isValid = jwtService.validate(token);

                if (isValid) {
                    String username = jwtService.extractUsername(token);
                    jwtService.setUserInRequest(username, request);

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, null);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                } else {
                    log.info("JWT Authentication Failed: token invalid");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    // TODO: Redirect to login
                    response.getWriter().write("Unauthorized request.");
                    return;
                }
            } catch (IOException e) {
                log.info("JWT Authentication filter exception: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // TODO: Redirect to login
                response.getWriter().write("Unauthorized request.");
                return;
            }
        }
        log.info("JWT Authentication filter completed");
      
        filterChain.doFilter(request, response);
       
    }
}
    
    
      

package com.example.UTSAPlaceBackend.util;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JWTAuthFilter  extends OncePerRequestFilter {

    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header is missing");
            return;
        }
        token = token.substring(7);
        try{
            boolean isValid = jwtService.validate(token);
        
        if (isValid){
            String username = jwtService.extractUsername(token);
            jwtService.setUserInRequest(username, request);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, null);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        


        }else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return;
        } 
        }catch (IOException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return;
        }
      
        filterChain.doFilter(request, response);
       
    }
}
    
    
      

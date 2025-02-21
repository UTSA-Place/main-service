package com.example.UTSAPlaceBackend.models;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authManager;


    @PostMapping("/register")
    public User register(@RequestBody User user) {
        log.info("Registering user: {}", user.getUsername());
        return authService.createUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        log.info("Logging in user: {}", user.getUsername());

        // Perform sprint authentication
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        if(authentication.isAuthenticated()) {
            return authService.login((User) authentication.getPrincipal());
        }

        // Failed authentication
        log.info("Failed authentication for user {}", user.getUsername());
        throw new RuntimeException("Unable to authenticate user");
    }

}

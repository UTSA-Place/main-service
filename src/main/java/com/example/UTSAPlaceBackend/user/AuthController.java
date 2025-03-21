package com.example.UTSAPlaceBackend.user;


import com.example.UTSAPlaceBackend.models.LoginResponse;
import com.example.UTSAPlaceBackend.models.User;
import com.example.UTSAPlaceBackend.util.exceptions.AuthenticationException;
import com.example.UTSAPlaceBackend.util.exceptions.EmailNotVerifiedException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/register")
    public User register(@RequestBody User user) throws Exception {
        log.info("Registering user: {}", user.getUsername());
        final User newUser = authService.register(user);
        log.info("User created: {}", user.getUsername());
        return newUser;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody User user) throws AuthenticationException, EmailNotVerifiedException {
        log.info("Logging in user: {}", user.getUsername());
        return authService.login(user);
    }


}

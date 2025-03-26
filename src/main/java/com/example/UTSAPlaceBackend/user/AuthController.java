package com.example.UTSAPlaceBackend.user;


import com.example.UTSAPlaceBackend.models.LoginRequest;
import com.example.UTSAPlaceBackend.models.LoginResponse;
import com.example.UTSAPlaceBackend.models.User;
import com.example.UTSAPlaceBackend.util.exceptions.AuthenticationException;
import com.example.UTSAPlaceBackend.util.exceptions.EmailNotVerifiedException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public User register(@Valid @RequestBody User user) throws Exception {
        log.info("Registering user: {}", user.getUsername());
        final User newUser = authService.register(user);
        log.info("User created: {}", user.getUsername());
        return newUser;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) throws AuthenticationException, EmailNotVerifiedException {
        log.info("Logging in user: {}", request.getUsername());
        final LoginResponse loginResponse = authService.login(request);
        log.info("User {} successfully logged in", request.getUsername());
        return loginResponse;
    }


}

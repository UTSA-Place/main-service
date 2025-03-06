package com.example.UTSAPlaceBackend.user;


import com.example.UTSAPlaceBackend.models.LoginResponse;
import com.example.UTSAPlaceBackend.models.User;
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

    private UserService userService;

    private AuthService authService;

    private AuthenticationManager authManager;


    @PostMapping("/register")
    public User register(@RequestBody User user) throws Exception {
        log.info("Registering user: {}", user.getUsername());
        // TODO: handle exception
        final User newUser = authService.register(user);
        log.info("User created: {}", user.getUsername());
        return newUser;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody User user) {
        log.info("Logging in user: {}", user.getUsername());
        // TODO: handle exceptions
        return authService.login(user);
    }


}

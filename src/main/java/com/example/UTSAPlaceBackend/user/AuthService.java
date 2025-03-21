package com.example.UTSAPlaceBackend.user;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.UTSAPlaceBackend.email.EmailVerificationService;
import com.example.UTSAPlaceBackend.models.LoginResponse;
import com.example.UTSAPlaceBackend.models.User;
import com.example.UTSAPlaceBackend.util.EmailValidator;
import com.example.UTSAPlaceBackend.util.JWTService;
import com.example.UTSAPlaceBackend.util.exceptions.UTSAPlaceException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

    private EmailValidator emailValidator;

    private EmailVerificationService emailVerificationService;

    private BCryptPasswordEncoder passwordEncoder;

    private AuthenticationManager authManager;

    private UserRepository userRepository;

    private JWTService jwtService;

    public LoginResponse login(User user) {

        // Perform spring authentication
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        // If user is not authenticated or user email is not verified, throw exception
        if(!authentication.isAuthenticated()) {
            throw new RuntimeException("Invalid credentials");
        }
        if (((User) authentication.getPrincipal()).isEnabled()) {
            throw new RuntimeException("User email not verified. " +
                        "Check you email for verification or get a new verification link.");
        }

        String token = jwtService.createToken2(user.getUsername());
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;

    }

    public User register(User user) throws Exception {

        final String email = user.getUsername();

        // Throw exception if user already exists
        if(userRepository.findByUsername(email).isPresent()) {
            //TODO: Specify error
            throw new UTSAPlaceException();
        }
        // Throw exception id email is not valid
        if(!emailValidator.test(email)) {
            // TODO:
            throw new UTSAPlaceException();
        }

        // Encrypt password before storing in DB
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Set user to disabled until email is verified
        user.setEnabled(false);
        // Save user to database

        try {
            emailVerificationService.sendEmailVerification(user);
        } catch(Exception e) {
            // TODO: email send failed not valid email or other mail error
            throw new UTSAPlaceException();
        }

        final User createdUser = userRepository.save(user);

        return user;
    }

}

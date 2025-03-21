package com.example.UTSAPlaceBackend.user;

import com.example.UTSAPlaceBackend.util.exceptions.AuthenticationException;
import com.example.UTSAPlaceBackend.util.exceptions.EmailNotVerifiedException;
import com.example.UTSAPlaceBackend.util.exceptions.RegistrationException;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

    private EmailValidator emailValidator;

    private EmailVerificationService emailVerificationService;

    private BCryptPasswordEncoder passwordEncoder;

    private AuthenticationManager authManager;

    private UserRepository userRepository;

    private JWTService jwtService;

    public LoginResponse login(User user) throws AuthenticationException, EmailNotVerifiedException {

        // Perform spring authentication
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        // If user is not authenticated throw exception
        if(!authentication.isAuthenticated()) {
                throw new AuthenticationException();
        }
        // Check is user email is verified
        if (!((User) authentication.getPrincipal()).isEnabled()) {
            throw new EmailNotVerifiedException();
        }

        String token = jwtService.createToken2(user.getUsername());
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;

    }

    public User register(User user) throws RegistrationException {

        final String email = user.getUsername();

        // Throw exception if user already exists
        if(userRepository.findByUsername(email).isPresent()) {
            log.info("Registration failed for user: {}. User already exists.", email);
            throw new RegistrationException("User already exists");
        }
        // Throw exception if email is not a valid UTSA email
        if(!emailValidator.test(email)) {
            log.info("Registration failed for user: {}. User email not valid UTSA email.", email);
            throw new RegistrationException("Not a valid UTSA email");
        }

        // Encrypt password before storing in DB
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Set user to disabled until email is verified
        user.setEnabled(false);

        // Send email verification email to user
        emailVerificationService.sendEmailVerification(user);

        // Save user to database
        final User createdUser = userRepository.save(user);

        // Hide encrypted password: DO NOT REMOVE!
        createdUser.setPassword(null);

        return createdUser;
    }

}

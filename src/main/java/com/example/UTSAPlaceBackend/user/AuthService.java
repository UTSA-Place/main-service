package com.example.UTSAPlaceBackend.user;

import com.example.UTSAPlaceBackend.models.LoginRequest;
import com.example.UTSAPlaceBackend.util.exceptions.AuthenticationException;
import com.example.UTSAPlaceBackend.util.exceptions.EmailNotVerifiedException;
import com.example.UTSAPlaceBackend.util.exceptions.RegistrationException;
import jakarta.mail.SendFailedException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.angus.mail.smtp.SMTPAddressFailedException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.UTSAPlaceBackend.email.EmailVerificationService;
import com.example.UTSAPlaceBackend.models.LoginResponse;
import com.example.UTSAPlaceBackend.models.User;
import com.example.UTSAPlaceBackend.util.EmailValidator;
import com.example.UTSAPlaceBackend.JWT.JWTService;
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

    public LoginResponse login(LoginRequest request) throws AuthenticationException, EmailNotVerifiedException {

        // Perform spring authentication
        Authentication authentication = null;
        log.info("Authenticating user: {}", request.getUsername());
        try {
            authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            // If user is disabled, user must verify email before logging in
        } catch (DisabledException e) {
            log.info("User {} not verified", request.getUsername());
            throw new EmailNotVerifiedException("Email not verified");
            // Invalid credentials were used
        } catch (BadCredentialsException e) {
            log.info("Invalid username or password: {}", request);
            throw new AuthenticationException("Invalid username or password");
        }

        // If user is not authenticated throw exception
        if(authentication != null && !authentication.isAuthenticated()) {
            log.info("Unable to authenticate user: {}", request);
                throw new AuthenticationException("User not authenticated");
        }

        String token = jwtService.createToken2(request.getUsername());
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;

    }

    public User register(User user) throws UTSAPlaceException {

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

        // Save user to database
        final User createdUser = userRepository.save(user);

        try {
            // Try to send verification email to user.
            // May fail due to failed connection or invalid email address.
            emailVerificationService.sendEmailVerification(user);
        } catch (MailSendException e) {
            log.info("Mail send exception: {}", e.getFailedMessages());
            // Find root cause of MailSendException
            for(Exception ex: e.getMessageExceptions()) {
                // If root exception is due to invalid SMTP address
                if(ex instanceof SendFailedException && ((SendFailedException) ex)
                        .getNextException() instanceof SMTPAddressFailedException) {
                    log.info("Invalid user email address: {}", user.getUsername());
                    // Delete user since email is invalid
                    userRepository.deleteById(user.getUsername());
                    throw new RegistrationException("Invalid email address");
                }
            }
            throw e;
        } catch(Exception e) {
            // Handle other unexpected exceptions
            log.info("Unexpected exception occurred sending email verification: {}", (Object) e.getStackTrace());
            throw new UTSAPlaceException("An unexpected error occurred");
        }

        // Hide encrypted password: DO NOT REMOVE!
        createdUser.setPassword(null);

        return createdUser;
    }

}

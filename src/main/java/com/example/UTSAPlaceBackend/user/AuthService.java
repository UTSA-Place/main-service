package com.example.UTSAPlaceBackend.user;

import com.example.UTSAPlaceBackend.UtsaPlaceBackendApplication;
import com.example.UTSAPlaceBackend.util.exceptions.AuthenticationException;
import com.example.UTSAPlaceBackend.util.exceptions.EmailNotVerifiedException;
import com.example.UTSAPlaceBackend.util.exceptions.RegistrationException;
import jakarta.mail.SendFailedException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.angus.mail.smtp.SMTPAddressFailedException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
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
        Authentication authentication;
        log.info("Authenticating user: {}", user.getUsername());
        try {
            authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            // Id user is disabled, user must verify email before logging in
        } catch (DisabledException e) {
            log.info("User {} not verified", user.getUsername());
            throw new EmailNotVerifiedException("Email not verified");
        }

        // If user is not authenticated throw exception
        if(authentication != null && !authentication.isAuthenticated()) {
            log.info("User {} not authenticated", user.getUsername());
                throw new AuthenticationException("User not authenticated");
        }

        log.info("auth done");
        String token = jwtService.createToken2(user.getUsername());
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
        // Set user to disabled until email is verified
        user.setEnabled(false);

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
            log.info("Unexpected exception occurred during user email verification: {}", (Object) e.getStackTrace());
            throw new UTSAPlaceException(e.getMessage());
        }

        // Hide encrypted password: DO NOT REMOVE!
        createdUser.setPassword(null);

        return createdUser;
    }

}

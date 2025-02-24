package com.example.UTSAPlaceBackend.user;

import com.example.UTSAPlaceBackend.models.LoginResponse;
import com.example.UTSAPlaceBackend.models.User;
import com.example.UTSAPlaceBackend.util.EmailValidator;
import com.example.UTSAPlaceBackend.util.exceptions.UTSAPlaceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private EmailValidator emailValidator;

    public LoginResponse login(User user) {
        // TODO: JWT create token
        return null;
    }

    @Override
    public User loadUserByUsername(final String email) throws UsernameNotFoundException {
        return userRepository.findByUsername(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with email %s not found", email)));
    }

    public User createUser(User user) throws Exception {

        final String email = user.getUsername();
        // Verify if user already exists in DB
        boolean userExists = userRepository.findByUsername(email).isPresent();
        // Validate that is valid email
        boolean isValidEmail = emailValidator.test(email);
        if (!isValidEmail) {
            throw new UTSAPlaceException();
        } else if (userExists) {
            throw new UTSAPlaceException();
        }
        // Encrypt password before storing in DB
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Set user to disabled until email is verified
        user.setEnabled(false);
        // Save user to database
        return userRepository.save(user);
    }

}

package com.example.UTSAPlaceBackend.auth;


import com.example.UTSAPlaceBackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.createUser(user);
    }

    public String login(User user) {
        // TODO: Call jwt class and generate token with user object. Return token
        return "token";
    }

}

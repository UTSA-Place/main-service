package com.example.UTSAPlaceBackend.auth;


import com.example.UTSAPlaceBackend.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) throws Exception {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Saving user: {}", user.getUsername());
        return userRepository.save(user);
    }

    public List<User> createUsers(List<User> users) {
        return userRepository.saveAll(users);
    }

    public String login(User user) {
        // TODO: Call jwt class and generate token with user object. Return token
        return "token";
    }

}

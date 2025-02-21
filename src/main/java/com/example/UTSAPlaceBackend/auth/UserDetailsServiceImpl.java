package com.example.UTSAPlaceBackend.auth;

import com.example.UTSAPlaceBackend.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findById(username);
        if(userOpt.isPresent()) {
            return userOpt.get();
        }
        log.info("User repository unable to find user: {}", username);
        // TODO: Error or http status
        return null;
    }

}

package com.example.UTSAPlaceBackend.emailConfirmation;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailConfirmationService {

    public void saveConfirmation(EmailConfirmation emailConfirmation) {

    }

    public Optional<EmailConfirmation> findByToken(String token) {
        return Optional.empty();
    }
}

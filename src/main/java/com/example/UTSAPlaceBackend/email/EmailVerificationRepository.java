package com.example.UTSAPlaceBackend.email;

import com.example.UTSAPlaceBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String> {
    Optional<EmailVerification> findByToken(String token);

    Optional<EmailVerification> findEmailVerificationByUser(User user);

}

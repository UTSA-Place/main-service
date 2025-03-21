package com.example.UTSAPlaceBackend.email;

import com.example.UTSAPlaceBackend.config.PasswordEncoder;
import com.example.UTSAPlaceBackend.models.User;
import com.example.UTSAPlaceBackend.user.UserRepository;
import com.example.UTSAPlaceBackend.util.exceptions.EmailVerificationException;
import com.example.UTSAPlaceBackend.util.exceptions.UTSAPlaceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class EmailVerificationService {

    @Value("${BASE_URL:127.0.0.1:8080/}")
    private String BASE_URL;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailVerificationRepository verificationRepository;

    @Autowired
    private UserRepository userRepository;

    public User verifyEmail(final String token) throws EmailVerificationException {

        // Find verification by token if it exists
        final EmailVerification verification = verificationRepository.findByToken(token).orElseThrow(() -> {
            log.info("Email verification not found for token: {}", token);
            return new EmailVerificationException("Email verification does not exist");
        });

        // Check if email verification has expired
        if(verification.getExpiration().isAfter(LocalDateTime.now())) {
            throw new EmailVerificationException("Email verification expired");
        }

        // Get user from verification and update to enabled marking user as verified
        User user = verification.getUser();
        user.setEnabled(true);

        // Save updated user
        User updatedUser = userRepository.save(user);
        // HIDE PASSWORD: DO NOT REMOVE!!
        user.setPassword(null);
        return updatedUser;
    }

    public void sendEmailVerification(final User user) throws MailSendException {
        // Find previous email verification created
        final Optional<EmailVerification> verificationOpt = verificationRepository.findEmailVerificationByUser(user);
        // If previous confirmation emil was sent delete prev confirmation and send new one
        verificationOpt.ifPresent(verification -> verificationRepository.delete(verification));
        // Create new verification link
        final EmailVerification newVerification = new EmailVerification(UUID.randomUUID().toString(), LocalDateTime.now(), user);
        verificationRepository.save(newVerification);

        // Send email with verification link
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@utsaplace.com");
        message.setTo(user.getUsername());
        message.setSubject("Verify email address");
        message.setText(String.format("Hello %s%s%s", BASE_URL, "verification/", newVerification.getToken()));

        mailSender.send(message);
    }


}

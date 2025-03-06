package com.example.UTSAPlaceBackend.email;

import com.example.UTSAPlaceBackend.config.PasswordEncoder;
import com.example.UTSAPlaceBackend.models.User;
import com.example.UTSAPlaceBackend.user.UserRepository;
import com.example.UTSAPlaceBackend.util.exceptions.UTSAPlaceException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

    @Autowired
    PasswordEncoder passwordEncoder;

    private EmailVerification findEmailVerification(final String token) {
        return verificationRepository.findByToken(token).orElseThrow(() ->
                // TODO: specify error
                new UTSAPlaceException()
        );
    }

    public User verifyEmail(final String token) {

        // Get verification if exists
        EmailVerification verification = findEmailVerification(token);

        // Check if email verification has expired
        if(verification.getExpiration().isAfter(LocalDateTime.now())) {
            throw new UTSAPlaceException();
        }

        // Get user from verification and update to enabled
        User user = verification.getUser();
        user.setEnabled(true);
        // Save updated user
        return userRepository.save(user);
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
        message.setTo("briplomo@gmail.com");
        message.setSubject("Verify email address");
        message.setText(String.format("Hello %s%s%s", BASE_URL, "verification/", newVerification.getToken()));

        mailSender.send(message);
    }


}

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

    @Value("${BASE_URL}")
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
            log.info("Email verification token not found: {}", token);
            return new EmailVerificationException();
        });

        // Check is user is already verified
        User user = verification.getUser();
        if(user.isEnabled()) {
            log.info("User {} has already been verified.", user.getUsername());
            throw new EmailVerificationException("User already verified");
        }

        // Check if email verification has expired
        if(verification.getExpiration().isAfter(LocalDateTime.now())) {
            throw new EmailVerificationException("Email verification expired");
        }

        // Update user to enabled marking user as verified
        user.setEnabled(true);

        // Save updated user as enabled
        User updatedUser = userRepository.save(user);
        log.info("User {} has been verified.", updatedUser.getUsername());

        // HIDE PASSWORD: DO NOT REMOVE!!
        user.setPassword(null);
        return updatedUser;
    }

    public void sendEmailVerification(final User user) throws MailSendException {
        // Find previous email verification if one exists already
        final Optional<EmailVerification> verificationOpt = verificationRepository.findEmailVerificationByUser(user);
        // If previous confirmation email was sent delete previous verification
        verificationOpt.ifPresent(verification -> verificationRepository.delete(verification));
        // Create new email verification
        final EmailVerification newVerification = new EmailVerification(UUID.randomUUID().toString(), LocalDateTime.now(), user);
        verificationRepository.save(newVerification);

        // Link to be sent
        String verificationLink = BASE_URL + "/verify/" + newVerification.getToken();

        // Send email with verification link
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@utsaplace.com");
        message.setTo(user.getUsername());
        message.setSubject("Verify email address");
        message.setText(String.format("Hello %s %s. Please verify you email at the link below!\n%s",
                user.getFirstName(), user.getLastName(), verificationLink));

        mailSender.send(message);
    }


}

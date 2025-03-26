package com.example.UTSAPlaceBackend.email;

import com.example.UTSAPlaceBackend.models.User;
import com.example.UTSAPlaceBackend.util.exceptions.EmailVerificationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/verify")
public class EmailVerificationController {

    private EmailVerificationService emailVerificationService;

    @GetMapping("/{token}")
    @ResponseBody
    public String verifyEmail(@PathVariable String token) throws EmailVerificationException {
        log.info("Verifying user with token {}", token);
        User updatedUser = emailVerificationService.verifyEmail(token);
        // TODO: Shaun -  Send html file instead of raw string. Look at FileServerController
        // Temporarily send raw string as response
        final String html = String.format(
                "<html><body><h1>Congrats %s %s!</h1>Your email has been verified!</body></html>",
                updatedUser.getFirstName(), updatedUser.getLastName());
        return html;
    }
}

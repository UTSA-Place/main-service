package com.example.UTSAPlaceBackend.email;

import com.example.UTSAPlaceBackend.models.User;
import com.example.UTSAPlaceBackend.util.exceptions.EmailVerificationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/api/verify")
public class EmailVerificationController {

    private EmailVerificationService emailVerificationService;

    @GetMapping("/{token}")
    public String verifyEmail(@PathVariable String token) throws EmailVerificationException {
        User updatedUser = emailVerificationService.verifyEmail(token);
        // TODO: Shaun -  Send html file instead of raw string. Look at FileServerController
        // Temporarily send raw string as response
        final String html = String.format(
                "<html><body><h1>Congrats %s %s!</h1>Your email has been verified!</body></html>",
                updatedUser.getFirstName(), updatedUser.getLastName());
        return html;
    }
}

package com.example.UTSAPlaceBackend.util.exceptions;

public class EmailVerificationException extends UTSAPlaceException {

    public EmailVerificationException(String message) {
        super(String.format("Email verification failed: %s", message));
    }

    EmailVerificationException() {
        super("Email verification failed.");
    }
}

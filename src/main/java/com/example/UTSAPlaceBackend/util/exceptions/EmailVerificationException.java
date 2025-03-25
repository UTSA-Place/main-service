package com.example.UTSAPlaceBackend.util.exceptions;

public class EmailVerificationException extends UTSAPlaceException {

    public EmailVerificationException(String message) {
        super(String.format(message));
    }

    public EmailVerificationException() {
        super("Email verification failed.");
    }
}

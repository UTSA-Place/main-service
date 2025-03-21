package com.example.UTSAPlaceBackend.util.exceptions;

public class EmailNotVerifiedException extends UTSAPlaceException {
    public EmailNotVerifiedException(String message) {
        super(message);
    }

    public EmailNotVerifiedException() {
        super("Email not verified");
    }
}

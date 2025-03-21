package com.example.UTSAPlaceBackend.util.exceptions;

/**
 * Exception to be thrown when performing authentication process aka logging in.
 * */
public class AuthenticationException extends UTSAPlaceException {
    public AuthenticationException() {
        super("Invalid username or password");
    }

    public AuthenticationException(final String message) {
        super(message);
    }
}

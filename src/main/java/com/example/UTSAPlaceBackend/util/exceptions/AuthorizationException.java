package com.example.UTSAPlaceBackend.util.exceptions;

/**
 * Exception to be thrown when an unauthorized action is attempted. Eg: Accessing admin
 * endpoints without appropriate role accessing someone else's account, etc.
 * */
public class AuthorizationException extends UTSAPlaceException {
    public AuthorizationException() {
        super("Request not authorized.");
    }
}

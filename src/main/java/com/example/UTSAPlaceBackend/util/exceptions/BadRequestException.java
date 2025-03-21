package com.example.UTSAPlaceBackend.util.exceptions;

/**
 * Exception to be thrown when the request body does not match what is expected.
 * */
public class BadRequestException extends UTSAPlaceException {
    public BadRequestException(String message) {
        super(String.format("Malformed request: %s", message));
    }
}

package com.example.UTSAPlaceBackend.util.exceptions;

/**
 * Exception to be thrown when registration attempt fails for some known reason.
 * Ex: Invalid or non UTSA email, invalid password length, etc.
 * */
public class RegistrationException extends UTSAPlaceException {

    public RegistrationException() {
        super("Invalid registration credentials.");
    }

    public RegistrationException(final String message) {
        super(String.format(message));
    }
}

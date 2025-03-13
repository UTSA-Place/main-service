package com.example.UTSAPlaceBackend.util;

import java.util.function.Predicate;

import org.springframework.stereotype.Component;

@Component
public class EmailValidator implements Predicate<String> {
    private final String UTSA_EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@utsa.edu$";
    private final String UTSA_EMAIL_REGEX_2 = "^[a-zA-Z0-9._%+-]+@my.utsa.edu$";

    @Override
    public boolean test(String s) {
        // Check if the email matches the regex for UTSA email
        if (s == null || s.isEmpty()) {
            return false;
        }
        // Check if the email matches the regex for UTSA email
        return s.matches(UTSA_EMAIL_REGEX) || s.matches(UTSA_EMAIL_REGEX_2);
    }
}

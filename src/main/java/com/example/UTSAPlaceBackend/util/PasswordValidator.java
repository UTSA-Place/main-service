package com.example.UTSAPlaceBackend.util;

import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class PasswordValidator implements Predicate<String> {

    private final Pattern pattern = null;

    @Override
    public boolean test(String password) {
        // TODO Length of 8, 1 upper case letter, 1 number
        return password != null && password.length() >= 8;
    }
}

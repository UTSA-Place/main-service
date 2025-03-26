package com.example.UTSAPlaceBackend.user;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN");

    private final String value;

    UserRole(final String value) {
        this.value = value;
    }

}

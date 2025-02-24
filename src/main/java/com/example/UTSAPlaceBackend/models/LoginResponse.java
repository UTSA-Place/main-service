package com.example.UTSAPlaceBackend.models;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String errorMessage;
}

package com.example.UTSAPlaceBackend.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {

    @NotEmpty(message="Username required")
    private String username;

    @NotEmpty(message="Password required")
    private String password;

}

package com.example.UTSAPlaceBackend.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class LoginResponse {
    private String token;
    // TODO: Implement refresh token
    private String refreshToken;
}

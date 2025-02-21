package com.example.UTSAPlaceBackend.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class User implements UserDetails {

    private String firstName;
    private String lastName;
    private String password;
    private String username;
    private Collection<GrantedAuthority> authorities;

}

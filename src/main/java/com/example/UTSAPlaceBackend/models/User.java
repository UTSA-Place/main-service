package com.example.UTSAPlaceBackend.models;

import java.util.Collection;
import java.util.Collections;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @NotEmpty(message="Email required")
    private String username;
    @NotEmpty(message="First name required")
    private String firstName;
    @NotEmpty(message="Last name required")
    private String lastName;
    private String password;
    private boolean enabled = false;
    private UserRole role = UserRole.USER;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getValue()));
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

 } 


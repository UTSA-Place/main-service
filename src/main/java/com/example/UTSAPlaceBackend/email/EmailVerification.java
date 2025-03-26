package com.example.UTSAPlaceBackend.email;

import com.example.UTSAPlaceBackend.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {


    @Id
    private String token;

    private LocalDateTime expiration;

    @OneToOne
    @JoinColumn(
            nullable = false,
            name="user",
            unique = true,
            referencedColumnName = "username"
    )
    private User user;

}

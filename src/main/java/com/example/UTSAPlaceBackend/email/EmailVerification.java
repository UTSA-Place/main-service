package com.example.UTSAPlaceBackend.email;

import com.example.UTSAPlaceBackend.models.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {


    @Id
    private String token;

    private LocalDateTime expiration;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name="user",
            unique = true
    )
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;

}

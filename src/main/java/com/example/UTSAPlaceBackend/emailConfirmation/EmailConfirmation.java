package com.example.UTSAPlaceBackend.emailConfirmation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;

@Entity
@Data
public class EmailConfirmation {

    @Getter
    @Id
    private Long id;
    private String token;

}

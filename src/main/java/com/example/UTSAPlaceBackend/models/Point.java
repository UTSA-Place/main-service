package com.example.UTSAPlaceBackend.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Data
public class Point implements Serializable {
    private int row;
    private int col;
}

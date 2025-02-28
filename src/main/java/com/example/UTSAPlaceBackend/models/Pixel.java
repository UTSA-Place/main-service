package com.example.UTSAPlaceBackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Pixel {

    @EmbeddedId
    @JsonUnwrapped
    private Point point;

    private String color;

    @JsonProperty("row")
    public void setInnerRow(int row) {
        if (point == null) {
            point = new Point();
        }
        point.setRow(row);
    }

    @JsonProperty("col")
    public void setInnerCol(int col) {
        if (point == null) {
            point = new Point();
        }
        point.setCol(col);
    }


}

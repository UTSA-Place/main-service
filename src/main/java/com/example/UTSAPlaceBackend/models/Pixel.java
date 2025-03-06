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
    private Point point;

    private String color;

    @JsonProperty("row")
    public void setInnerRow(int row) {
        System.out.println(row);
        if (this.point == null) {
            this.point = new Point();
        }
        this.point.setRow(row);
    }

    @JsonProperty("row")
    public int getInnerRow() {
        return point.getRow();
    }

    @JsonProperty("col")
    public void setInnerCol(int col) {
        System.out.println(col);
        if (point == null) {
            point = new Point();
        }
        point.setCol(col);
    }

    @JsonProperty("col")
    public int getInnerCol() {
        return point.getCol();
    }
}

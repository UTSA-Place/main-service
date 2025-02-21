package com.example.UTSAPlaceBackend.models;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class Canvas {

    private int length;

    private int width;

    private int [][] matrix;

    public void placePixel(int x, int y, String color) {}

    public void clearCanvas() {}

    public void removePixel(int x, int y, String color) {}


}

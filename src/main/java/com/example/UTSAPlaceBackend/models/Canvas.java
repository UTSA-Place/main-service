package com.example.UTSAPlaceBackend.models;

import lombok.Getter;

import java.util.List;

@Getter
public class Canvas {
    final int MATRIX_SIZE = 500;

    private final String[][] pixels = new String[MATRIX_SIZE][MATRIX_SIZE];

    public Canvas(List<Pixel> pixelList) {
        for(Pixel p: pixelList) {
            pixels[p.getPoint().getRow()][p.getPoint().getCol()] = p.getColor();
        }
    }

    public void putPixel(final Pixel pixel) {
        pixels[pixel.getPoint().getRow()][pixel.getPoint().getCol()] = pixel.getColor();
    }

    public Pixel getPixel(int row, int col) {
        Pixel p = new Pixel();
        p.setColor(pixels[row][col]);
        p.setInnerRow(row);
        p.setInnerCol(col);
        return p;

    }

}

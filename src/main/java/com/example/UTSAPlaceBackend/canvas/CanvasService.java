package com.example.UTSAPlaceBackend.canvas;

import com.example.UTSAPlaceBackend.models.Canvas;
import com.example.UTSAPlaceBackend.models.Pixel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CanvasService {

    private CanvasRepository canvasRepository;

    public Canvas getCanvas() {
       final List<Pixel> pixels = canvasRepository.findAll();
       return new Canvas(pixels);
    }

    public Pixel placePixel(Pixel pixel) {
        return canvasRepository.save(pixel);
    }
}

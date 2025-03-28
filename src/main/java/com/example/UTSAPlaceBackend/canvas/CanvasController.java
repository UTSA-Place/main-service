package com.example.UTSAPlaceBackend.canvas;

import com.example.UTSAPlaceBackend.models.Canvas;
import com.example.UTSAPlaceBackend.models.Pixel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/canvas")
@AllArgsConstructor
public class CanvasController {

    private CanvasService canvasService;

    @PostMapping("/place")
    public Pixel placePixel(@RequestBody Pixel pixel) {
        log.info("Pixel created: {}", pixel.toString());
        Pixel savedPixel = canvasService.placePixel(pixel);
        log.info("Pixel created: {}", savedPixel.toString());
        return pixel;
    }

    @GetMapping("/")
    public Canvas getCanvas() {
        return canvasService.getCanvas();
    }




}

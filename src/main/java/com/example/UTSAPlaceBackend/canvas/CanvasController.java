package com.example.UTSAPlaceBackend.canvas;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/canvas")
public class CanvasController {

    @PostMapping("/place")
    public void placePixel(@RequestBody int x, @RequestBody int y) {
        // TODO: Place pixel
    }




}

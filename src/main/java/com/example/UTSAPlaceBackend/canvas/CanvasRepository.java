package com.example.UTSAPlaceBackend.canvas;

import com.example.UTSAPlaceBackend.models.Pixel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanvasRepository extends JpaRepository<Pixel, String> {

}

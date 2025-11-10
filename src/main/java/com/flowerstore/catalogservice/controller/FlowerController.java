package com.flowerstore.catalogservice.controller;

import com.flowerstore.catalogservice.model.Flower;
import com.flowerstore.catalogservice.service.FlowerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flowers")
public class FlowerController {
    private final FlowerService service;

    public FlowerController(FlowerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Flower>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<Flower> create(@RequestBody Flower flower) {
        Flower saved = service.create(flower);
        return ResponseEntity.ok(saved);
    }
}
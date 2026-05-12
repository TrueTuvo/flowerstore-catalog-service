package com.flowerstore.catalogservice.controller;

import com.flowerstore.catalogservice.model.Flower;
import com.flowerstore.catalogservice.service.FlowerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/flowers")
public class FlowerController {
    private final FlowerService service;

    public FlowerController(FlowerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Flower>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color) {
        return ResponseEntity.ok(service.search(name, color));
    }

    @PostMapping
    public ResponseEntity<Flower> createFlower(@Valid @RequestBody Flower flower) {
        Flower savedFlower = service.saveFlower(flower);
        log.info("Created flower id={} name={}", savedFlower.getId(), savedFlower.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFlower);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flower> getFlowerById(@PathVariable Long id) {
        return service.getFlowerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flower> updateFlower(@PathVariable Long id, @Valid @RequestBody Flower flower) {
        return service.updateFlower(id, flower)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlower(@PathVariable Long id) {
        boolean deleted = service.deleteFlower(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
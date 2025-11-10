package com.flowerstore.catalogservice.service;

import com.flowerstore.catalogservice.model.Flower;
import com.flowerstore.catalogservice.repository.FlowerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlowerService {
    private final FlowerRepository repo;

    public FlowerService(FlowerRepository repo) {
        this.repo = repo;
    }

    public List<Flower> getAll() {
        return repo.findAll();
    }

    public Flower create(Flower f) {
        return repo.save(f);
    }

    public Optional<Flower> getFlowerById(Long id) {
        return repo.findById(id);
    }

    public Flower saveFlower(Flower flower) {
        return repo.save(flower);
    }

    public Optional<Flower> updateFlower(Long id, Flower flower) {
        return repo.findById(id).map(existing -> {
            existing.setName(flower.getName());
            existing.setColor(flower.getColor());
            existing.setPrice(flower.getPrice());
            return repo.save(existing);
        });
    }

    public boolean deleteFlower(Long id) {
        return repo.findById(id).map(flower -> {
            repo.delete(flower);
            return true;
        }).orElse(false);
    }
}

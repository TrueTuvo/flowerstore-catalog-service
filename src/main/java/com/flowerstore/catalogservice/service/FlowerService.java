package com.flowerstore.catalogservice.service;

import com.flowerstore.catalogservice.model.Flower;
import com.flowerstore.catalogservice.repository.FlowerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}

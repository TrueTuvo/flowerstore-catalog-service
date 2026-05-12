package com.flowerstore.catalogservice;

import com.flowerstore.catalogservice.model.Flower;
import com.flowerstore.catalogservice.repository.FlowerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class FlowerRepositoryTest {

    @Autowired
    private FlowerRepository flowerRepository;

    @Test
    void saveAndRetrieveFlower() {
        Flower flower = new Flower();
        flower.setName("Rose");
        flower.setColor("Red");
        flower.setPrice(10.5);
        flowerRepository.save(flower);

        List<Flower> flowers = flowerRepository.findAll();
        System.out.println(flowers);
    }
}

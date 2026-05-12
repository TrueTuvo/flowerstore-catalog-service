package com.flowerstore.catalogservice.repository;

import com.flowerstore.catalogservice.model.Flower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowerRepository extends JpaRepository<Flower, Long> {

    @Query("SELECT f FROM Flower f WHERE " +
            "(:name IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:color IS NULL OR LOWER(f.color) LIKE LOWER(CONCAT('%', :color, '%')))")
    List<Flower> search(@Param("name") String name, @Param("color") String color);
}
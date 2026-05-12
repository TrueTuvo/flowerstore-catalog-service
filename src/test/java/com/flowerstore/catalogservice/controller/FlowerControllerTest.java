package com.flowerstore.catalogservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowerstore.catalogservice.model.Flower;
import com.flowerstore.catalogservice.service.FlowerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlowerController.class)
class FlowerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FlowerService service;

    private Flower flower(Long id, String name, String color, Double price) {
        return Flower.builder().id(id).name(name).color(color).price(price).build();
    }

    @Test
    void getAll_returnsList() throws Exception {
        given(service.getAll()).willReturn(List.of(
                flower(1L, "Rose", "Red", 10.5),
                flower(2L, "Tulip", "Yellow", 7.0)
        ));

        mockMvc.perform(get("/api/flowers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Rose"))
                .andExpect(jsonPath("$[1].name").value("Tulip"));
    }

    @Test
    void getById_whenFound_returns200() throws Exception {
        given(service.getFlowerById(1L)).willReturn(Optional.of(flower(1L, "Rose", "Red", 10.5)));

        mockMvc.perform(get("/api/flowers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rose"))
                .andExpect(jsonPath("$.color").value("Red"))
                .andExpect(jsonPath("$.price").value(10.5));
    }

    @Test
    void getById_whenMissing_returns404() throws Exception {
        given(service.getFlowerById(99L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/flowers/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_validBody_returns201() throws Exception {
        Flower request = flower(null, "Rose", "Red", 10.5);
        Flower saved = flower(1L, "Rose", "Red", 10.5);
        given(service.saveFlower(any(Flower.class))).willReturn(saved);

        mockMvc.perform(post("/api/flowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rose"));
    }

    @Test
    void create_blankName_returns400() throws Exception {
        Flower invalid = flower(null, "  ", "Red", 10.5);

        mockMvc.perform(post("/api/flowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name cannot be blank"));

        verify(service, never()).saveFlower(any());
    }

    @Test
    void create_nullPrice_returns400() throws Exception {
        Flower invalid = flower(null, "Rose", "Red", null);

        mockMvc.perform(post("/api/flowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.price").value("Price is required"));

        verify(service, never()).saveFlower(any());
    }

    @Test
    void create_negativePrice_returns400() throws Exception {
        Flower invalid = flower(null, "Rose", "Red", -1.0);

        mockMvc.perform(post("/api/flowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.price").value("Price must be zero or positive"));

        verify(service, never()).saveFlower(any());
    }

    @Test
    void update_whenFound_returns200() throws Exception {
        Flower request = flower(null, "Rose", "Pink", 12.0);
        Flower updated = flower(1L, "Rose", "Pink", 12.0);
        given(service.updateFlower(eq(1L), any(Flower.class))).willReturn(Optional.of(updated));

        mockMvc.perform(put("/api/flowers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.color").value("Pink"))
                .andExpect(jsonPath("$.price").value(12.0));
    }

    @Test
    void update_whenMissing_returns404() throws Exception {
        Flower request = flower(null, "Rose", "Pink", 12.0);
        given(service.updateFlower(eq(99L), any(Flower.class))).willReturn(Optional.empty());

        mockMvc.perform(put("/api/flowers/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_invalidBody_returns400() throws Exception {
        Flower invalid = flower(null, "", "Red", 10.5);

        mockMvc.perform(put("/api/flowers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name cannot be blank"));

        verify(service, never()).updateFlower(any(), any());
    }

    @Test
    void delete_whenFound_returns204() throws Exception {
        given(service.deleteFlower(1L)).willReturn(true);

        mockMvc.perform(delete("/api/flowers/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_whenMissing_returns404() throws Exception {
        given(service.deleteFlower(99L)).willReturn(false);

        mockMvc.perform(delete("/api/flowers/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}
package com.cpu.management.controller;

import com.cpu.management.dto.TechnologyCreateDTO;
import com.cpu.management.dto.TechnologyDTO;
import com.cpu.management.service.TechnologyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TechnologyControllerTest {

    @Mock
    private TechnologyService technologyService;

    @InjectMocks
    private TechnologyController technologyController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private TechnologyDTO sampleDTO;
    private TechnologyCreateDTO sampleCreateDTO;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(technologyController).build();
        objectMapper = new ObjectMapper();

        sampleId = UUID.randomUUID();

        sampleDTO = TechnologyDTO.builder()
                .id(sampleId)
                .name("SMT")
                .description("Simultaneous Multithreading")
                .releaseYear(2017)
                .cpuCount(3)
                .build();

        sampleCreateDTO = TechnologyCreateDTO.builder()
                .name("SMT")
                .description("Simultaneous Multithreading")
                .releaseYear(2017)
                .build();
    }

    @Test
    void shouldCreateTechnology() throws Exception {
        when(technologyService.addTechnology(any(TechnologyCreateDTO.class))).thenReturn(sampleDTO);

        mockMvc.perform(post("/api/v1/technologies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("SMT"))
                .andExpect(jsonPath("$.description").value("Simultaneous Multithreading"))
                .andExpect(jsonPath("$.releaseYear").value(2017));
    }

    @Test
    void shouldGetTechnologyById() throws Exception {
        when(technologyService.getTechnologyById(sampleId)).thenReturn(Optional.of(sampleDTO));

        mockMvc.perform(get("/api/v1/technologies/{id}", sampleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("SMT"));
    }

    @Test
    void shouldReturn404WhenTechnologyNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(technologyService.getTechnologyById(nonExistentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/technologies/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllTechnologies() throws Exception {
        TechnologyDTO dto2 = TechnologyDTO.builder()
                .id(UUID.randomUUID())
                .name("DDR5")
                .description("Latest DDR memory")
                .releaseYear(2020)
                .build();

        when(technologyService.getAllTechnologies()).thenReturn(Arrays.asList(sampleDTO, dto2));

        mockMvc.perform(get("/api/v1/technologies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("SMT"))
                .andExpect(jsonPath("$[1].name").value("DDR5"));
    }

    @Test
    void shouldUpdateTechnology() throws Exception {
        TechnologyDTO updatedDTO = TechnologyDTO.builder()
                .id(sampleId)
                .name("SMT 2.0")
                .description("Updated SMT")
                .releaseYear(2023)
                .build();

        when(technologyService.updateTechnology(eq(sampleId), any(TechnologyCreateDTO.class)))
                .thenReturn(updatedDTO);

        mockMvc.perform(put("/api/v1/technologies/{id}", sampleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("SMT 2.0"));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentTechnology() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(technologyService.updateTechnology(eq(nonExistentId), any(TechnologyCreateDTO.class)))
                .thenReturn(null);

        mockMvc.perform(put("/api/v1/technologies/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCreateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteTechnology() throws Exception {
        doNothing().when(technologyService).deleteTechnologyById(sampleId);

        mockMvc.perform(delete("/api/v1/technologies/{id}", sampleId))
                .andExpect(status().isNoContent());

        verify(technologyService).deleteTechnologyById(sampleId);
    }
}

package com.cpu.management.controller;

import com.cpu.management.dto.ManufacturerCreateDTO;
import com.cpu.management.dto.ManufacturerDTO;
import com.cpu.management.service.ManufacturerService;
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
class ManufacturerControllerTest {

    @Mock
    private ManufacturerService manufacturerService;

    @InjectMocks
    private ManufacturerController manufacturerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ManufacturerDTO sampleDTO;
    private ManufacturerCreateDTO sampleCreateDTO;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(manufacturerController).build();
        objectMapper = new ObjectMapper();

        sampleId = UUID.randomUUID();

        sampleDTO = ManufacturerDTO.builder()
                .id(sampleId)
                .name("Intel")
                .country("USA")
                .foundedYear(1968)
                .cpuCount(5)
                .build();

        sampleCreateDTO = ManufacturerCreateDTO.builder()
                .name("Intel")
                .country("USA")
                .foundedYear(1968)
                .build();
    }

    @Test
    void shouldCreateManufacturer() throws Exception {
        when(manufacturerService.addManufacturer(any(ManufacturerCreateDTO.class))).thenReturn(sampleDTO);

        mockMvc.perform(post("/api/v1/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Intel"))
                .andExpect(jsonPath("$.country").value("USA"))
                .andExpect(jsonPath("$.foundedYear").value(1968));
    }

    @Test
    void shouldGetManufacturerById() throws Exception {
        when(manufacturerService.getManufacturerById(sampleId)).thenReturn(Optional.of(sampleDTO));

        mockMvc.perform(get("/api/v1/manufacturers/{id}", sampleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Intel"));
    }

    @Test
    void shouldReturn404WhenManufacturerNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(manufacturerService.getManufacturerById(nonExistentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/manufacturers/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllManufacturers() throws Exception {
        ManufacturerDTO dto2 = ManufacturerDTO.builder()
                .id(UUID.randomUUID())
                .name("AMD")
                .country("USA")
                .foundedYear(1969)
                .build();

        when(manufacturerService.getAllManufacturers()).thenReturn(Arrays.asList(sampleDTO, dto2));

        mockMvc.perform(get("/api/v1/manufacturers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Intel"))
                .andExpect(jsonPath("$[1].name").value("AMD"));
    }

    @Test
    void shouldUpdateManufacturer() throws Exception {
        ManufacturerDTO updatedDTO = ManufacturerDTO.builder()
                .id(sampleId)
                .name("Intel Corporation")
                .country("United States")
                .foundedYear(1968)
                .build();

        when(manufacturerService.updateManufacturer(eq(sampleId), any(ManufacturerCreateDTO.class)))
                .thenReturn(updatedDTO);

        mockMvc.perform(put("/api/v1/manufacturers/{id}", sampleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Intel Corporation"));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentManufacturer() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(manufacturerService.updateManufacturer(eq(nonExistentId), any(ManufacturerCreateDTO.class)))
                .thenReturn(null);

        mockMvc.perform(put("/api/v1/manufacturers/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCreateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteManufacturer() throws Exception {
        doNothing().when(manufacturerService).deleteManufacturerById(sampleId);

        mockMvc.perform(delete("/api/v1/manufacturers/{id}", sampleId))
                .andExpect(status().isNoContent());

        verify(manufacturerService).deleteManufacturerById(sampleId);
    }
}

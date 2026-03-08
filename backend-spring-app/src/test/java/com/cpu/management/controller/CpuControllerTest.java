package com.cpu.management.controller;

import com.cpu.management.dto.CpuCreateDTO;
import com.cpu.management.dto.CpuDTO;
import com.cpu.management.service.CpuService;
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
class CpuControllerTest {

    @Mock
    private CpuService cpuService;

    @InjectMocks
    private CpuController cpuController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CpuDTO sampleCpuDTO;
    private CpuCreateDTO sampleCreateDTO;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cpuController).build();
        objectMapper = new ObjectMapper();

        sampleId = UUID.randomUUID();

        sampleCpuDTO = CpuDTO.builder()
                .id(sampleId)
                .model("Ryzen 9 7950X")
                .cores(16)
                .threads(32)
                .frequencyGhz(4.5)
                .manufacturerName("AMD")
                .technologyNames(List.of("SMT"))
                .build();

        sampleCreateDTO = CpuCreateDTO.builder()
                .model("Ryzen 9 7950X")
                .cores(16)
                .threads(32)
                .frequencyGhz(4.5)
                .build();
    }

    @Test
    void shouldCreateCpu() throws Exception {
        when(cpuService.addCpu(any(CpuCreateDTO.class))).thenReturn(sampleCpuDTO);

        mockMvc.perform(post("/api/v1/cpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model").value("Ryzen 9 7950X"))
                .andExpect(jsonPath("$.cores").value(16))
                .andExpect(jsonPath("$.threads").value(32))
                .andExpect(jsonPath("$.frequencyGhz").value(4.5));
    }

    @Test
    void shouldGetCpuById() throws Exception {
        when(cpuService.getCpuById(sampleId)).thenReturn(Optional.of(sampleCpuDTO));

        mockMvc.perform(get("/api/v1/cpus/{id}", sampleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Ryzen 9 7950X"))
                .andExpect(jsonPath("$.manufacturerName").value("AMD"));
    }

    @Test
    void shouldReturn404WhenCpuNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(cpuService.getCpuById(nonExistentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/cpus/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllCpus() throws Exception {
        CpuDTO cpu2 = CpuDTO.builder()
                .id(UUID.randomUUID())
                .model("Core i9-13900K")
                .cores(24)
                .threads(32)
                .frequencyGhz(3.0)
                .build();

        when(cpuService.getAllCpus()).thenReturn(Arrays.asList(sampleCpuDTO, cpu2));

        mockMvc.perform(get("/api/v1/cpus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].model").value("Ryzen 9 7950X"))
                .andExpect(jsonPath("$[1].model").value("Core i9-13900K"));
    }

    @Test
    void shouldUpdateCpu() throws Exception {
        CpuDTO updatedDTO = CpuDTO.builder()
                .id(sampleId)
                .model("Ryzen 9 7950X3D")
                .cores(16)
                .threads(32)
                .frequencyGhz(4.2)
                .build();

        when(cpuService.updateCpu(eq(sampleId), any(CpuCreateDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/v1/cpus/{id}", sampleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Ryzen 9 7950X3D"));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentCpu() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(cpuService.updateCpu(eq(nonExistentId), any(CpuCreateDTO.class))).thenReturn(null);

        mockMvc.perform(put("/api/v1/cpus/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCreateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteCpu() throws Exception {
        doNothing().when(cpuService).deleteCpuById(sampleId);

        mockMvc.perform(delete("/api/v1/cpus/{id}", sampleId))
                .andExpect(status().isNoContent());

        verify(cpuService).deleteCpuById(sampleId);
    }

    @Test
    void shouldReturnEmptyListWhenNoCpus() throws Exception {
        when(cpuService.getAllCpus()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/cpus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

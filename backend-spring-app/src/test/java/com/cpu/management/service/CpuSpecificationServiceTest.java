package com.cpu.management.service;

import com.cpu.management.domain.CpuSpecification;
import com.cpu.management.dto.CpuSpecificationCreateDTO;
import com.cpu.management.dto.CpuSpecificationDTO;
import com.cpu.management.mapper.EntityMapper;
import com.cpu.management.repository.CpuSpecificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CpuSpecificationServiceTest {

    @Mock
    private CpuSpecificationRepository cpuSpecificationRepository;
    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private CpuSpecificationService cpuSpecificationService;

    private CpuSpecification sampleSpec;
    private CpuSpecificationDTO sampleSpecDTO;
    private CpuSpecificationCreateDTO sampleCreateDTO;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();

        sampleSpec = new CpuSpecification(64, 1024, 64, 170, "AM5");

        sampleSpecDTO = CpuSpecificationDTO.builder()
                .id(sampleId)
                .cacheL1KB(64)
                .cacheL2KB(1024)
                .cacheL3MB(64)
                .tdpWatts(170)
                .socketType("AM5")
                .build();

        sampleCreateDTO = CpuSpecificationCreateDTO.builder()
                .cacheL1KB(64)
                .cacheL2KB(1024)
                .cacheL3MB(64)
                .tdpWatts(170)
                .socketType("AM5")
                .build();
    }

    @Test
    void shouldAddSpecification() {
        when(entityMapper.toCpuSpecificationEntity(sampleCreateDTO)).thenReturn(sampleSpec);
        when(cpuSpecificationRepository.save(sampleSpec)).thenReturn(sampleSpec);
        when(entityMapper.toCpuSpecificationDTO(sampleSpec)).thenReturn(sampleSpecDTO);

        CpuSpecificationDTO result = cpuSpecificationService.addSpecification(sampleCreateDTO);

        assertNotNull(result);
        assertEquals("AM5", result.getSocketType());
        assertEquals(170, result.getTdpWatts());
        verify(cpuSpecificationRepository).save(sampleSpec);
    }

    @Test
    void shouldGetSpecificationById() {
        when(cpuSpecificationRepository.findById(sampleId)).thenReturn(Optional.of(sampleSpec));
        when(entityMapper.toCpuSpecificationDTO(sampleSpec)).thenReturn(sampleSpecDTO);

        Optional<CpuSpecificationDTO> result = cpuSpecificationService.getSpecificationById(sampleId);

        assertTrue(result.isPresent());
        assertEquals("AM5", result.get().getSocketType());
    }

    @Test
    void shouldReturnEmptyWhenSpecificationNotFound() {
        when(cpuSpecificationRepository.findById(sampleId)).thenReturn(Optional.empty());

        Optional<CpuSpecificationDTO> result = cpuSpecificationService.getSpecificationById(sampleId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGetAllSpecifications() {
        when(cpuSpecificationRepository.findAll()).thenReturn(List.of(sampleSpec));
        when(entityMapper.toCpuSpecificationDTOList(anyList())).thenReturn(List.of(sampleSpecDTO));

        List<CpuSpecificationDTO> result = cpuSpecificationService.getAllSpecifications();

        assertEquals(1, result.size());
    }

    @Test
    void shouldDeleteSpecification() {
        cpuSpecificationService.deleteSpecificationById(sampleId);

        verify(cpuSpecificationRepository).deleteById(sampleId);
    }

    @Test
    void shouldAddSpecificationEntity() {
        when(cpuSpecificationRepository.save(sampleSpec)).thenReturn(sampleSpec);

        CpuSpecification result = cpuSpecificationService.addSpecificationEntity(sampleSpec);

        assertNotNull(result);
        assertEquals("AM5", result.getSocketType());
    }

    @Test
    void shouldGetSpecificationEntityById() {
        when(cpuSpecificationRepository.findById(sampleId)).thenReturn(Optional.of(sampleSpec));

        Optional<CpuSpecification> result = cpuSpecificationService.getSpecificationEntityById(sampleId);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldGetAllSpecificationEntities() {
        when(cpuSpecificationRepository.findAll()).thenReturn(List.of(sampleSpec));

        List<CpuSpecification> result = cpuSpecificationService.getAllSpecificationEntities();

        assertEquals(1, result.size());
    }
}

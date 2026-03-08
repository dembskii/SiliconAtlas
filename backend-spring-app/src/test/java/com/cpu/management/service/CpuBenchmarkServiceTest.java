package com.cpu.management.service;

import com.cpu.management.domain.Cpu;
import com.cpu.management.domain.CpuBenchmark;
import com.cpu.management.dto.CpuBenchmarkCreateDTO;
import com.cpu.management.dto.CpuBenchmarkDTO;
import com.cpu.management.mapper.EntityMapper;
import com.cpu.management.repository.CpuBenchmarkRepository;
import com.cpu.management.repository.CpuRepository;
import com.cpu.management.repository.ManufacturerRepository;
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
class CpuBenchmarkServiceTest {

    @Mock
    private CpuBenchmarkRepository cpuBenchmarkRepository;
    @Mock
    private CpuRepository cpuRepository;
    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private CpuBenchmarkService cpuBenchmarkService;

    private CpuBenchmark sampleBenchmark;
    private CpuBenchmarkDTO sampleBenchmarkDTO;
    private CpuBenchmarkCreateDTO sampleCreateDTO;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();

        sampleBenchmark = new CpuBenchmark(2100, 30000, 45000, 38000.0, "2024-01-15");

        sampleBenchmarkDTO = CpuBenchmarkDTO.builder()
                .id(sampleId)
                .singleCoreScore(2100)
                .multiCoreScore(30000)
                .passmarkScore(45000)
                .cinebenchR23(38000.0)
                .testDate("2024-01-15")
                .build();

        sampleCreateDTO = CpuBenchmarkCreateDTO.builder()
                .singleCoreScore(2100)
                .multiCoreScore(30000)
                .passmarkScore(45000)
                .cinebenchR23(38000.0)
                .testDate("2024-01-15")
                .build();
    }

    @Test
    void shouldAddBenchmark() {
        when(entityMapper.toCpuBenchmarkEntity(sampleCreateDTO)).thenReturn(sampleBenchmark);
        when(cpuBenchmarkRepository.save(sampleBenchmark)).thenReturn(sampleBenchmark);
        when(entityMapper.toCpuBenchmarkDTO(sampleBenchmark)).thenReturn(sampleBenchmarkDTO);

        CpuBenchmarkDTO result = cpuBenchmarkService.addBenchmark(sampleCreateDTO);

        assertNotNull(result);
        assertEquals(2100, result.getSingleCoreScore());
        assertEquals(45000, result.getPassmarkScore());
        verify(cpuBenchmarkRepository).save(sampleBenchmark);
    }

    @Test
    void shouldGetBenchmarkById() {
        when(cpuBenchmarkRepository.findById(sampleId)).thenReturn(Optional.of(sampleBenchmark));
        when(entityMapper.toCpuBenchmarkDTO(sampleBenchmark)).thenReturn(sampleBenchmarkDTO);

        Optional<CpuBenchmarkDTO> result = cpuBenchmarkService.getBenchmarkById(sampleId);

        assertTrue(result.isPresent());
        assertEquals(2100, result.get().getSingleCoreScore());
    }

    @Test
    void shouldReturnEmptyWhenBenchmarkNotFound() {
        when(cpuBenchmarkRepository.findById(sampleId)).thenReturn(Optional.empty());

        Optional<CpuBenchmarkDTO> result = cpuBenchmarkService.getBenchmarkById(sampleId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGetAllBenchmarks() {
        when(cpuBenchmarkRepository.findAll()).thenReturn(List.of(sampleBenchmark));
        when(entityMapper.toCpuBenchmarkDTOList(anyList())).thenReturn(List.of(sampleBenchmarkDTO));

        List<CpuBenchmarkDTO> result = cpuBenchmarkService.getAllBenchmarks();

        assertEquals(1, result.size());
    }

    @Test
    void shouldDeleteBenchmark() {
        cpuBenchmarkService.deleteBenchmarkById(sampleId);

        verify(cpuBenchmarkRepository).deleteById(sampleId);
    }

    @Test
    void shouldUpdateBenchmark() {
        CpuBenchmarkCreateDTO updateDTO = CpuBenchmarkCreateDTO.builder()
                .singleCoreScore(2200)
                .multiCoreScore(31000)
                .passmarkScore(46000)
                .cinebenchR23(39000.0)
                .testDate("2024-02-01")
                .build();

        when(cpuBenchmarkRepository.findById(sampleId)).thenReturn(Optional.of(sampleBenchmark));
        when(cpuBenchmarkRepository.save(any(CpuBenchmark.class))).thenReturn(sampleBenchmark);
        when(entityMapper.toCpuBenchmarkDTO(any(CpuBenchmark.class))).thenReturn(sampleBenchmarkDTO);

        CpuBenchmarkDTO result = cpuBenchmarkService.updateBenchmark(sampleId, updateDTO);

        assertNotNull(result);
        verify(cpuBenchmarkRepository).save(any(CpuBenchmark.class));
    }

    @Test
    void shouldReturnNullWhenUpdatingNonExistentBenchmark() {
        when(cpuBenchmarkRepository.findById(sampleId)).thenReturn(Optional.empty());

        CpuBenchmarkDTO result = cpuBenchmarkService.updateBenchmark(sampleId, sampleCreateDTO);

        assertNull(result);
    }

    @Test
    void shouldAddBenchmarkToCpu() {
        UUID cpuId = UUID.randomUUID();
        Cpu cpu = new Cpu("Ryzen 9 7950X", 16, 32, 4.5);

        when(cpuRepository.findById(cpuId)).thenReturn(Optional.of(cpu));
        when(entityMapper.toCpuBenchmarkEntity(sampleCreateDTO)).thenReturn(sampleBenchmark);
        when(cpuBenchmarkRepository.save(any(CpuBenchmark.class))).thenReturn(sampleBenchmark);
        when(entityMapper.toCpuBenchmarkDTO(sampleBenchmark)).thenReturn(sampleBenchmarkDTO);

        CpuBenchmarkDTO result = cpuBenchmarkService.addBenchmarkToCpu(cpuId, sampleCreateDTO);

        assertNotNull(result);
        verify(cpuBenchmarkRepository).save(any(CpuBenchmark.class));
    }

    @Test
    void shouldThrowWhenAddingBenchmarkToNonExistentCpu() {
        UUID cpuId = UUID.randomUUID();
        when(cpuRepository.findById(cpuId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> cpuBenchmarkService.addBenchmarkToCpu(cpuId, sampleCreateDTO));
    }

    @Test
    void shouldAddBenchmarkEntity() {
        sampleBenchmark.setCpu(null);
        when(cpuBenchmarkRepository.save(sampleBenchmark)).thenReturn(sampleBenchmark);

        CpuBenchmark result = cpuBenchmarkService.addBenchmarkEntity(sampleBenchmark);

        assertNotNull(result);
        verify(cpuBenchmarkRepository).save(sampleBenchmark);
    }

    @Test
    void shouldGetBenchmarkEntityById() {
        when(cpuBenchmarkRepository.findById(sampleId)).thenReturn(Optional.of(sampleBenchmark));

        Optional<CpuBenchmark> result = cpuBenchmarkService.getBenchmarkEntityById(sampleId);

        assertTrue(result.isPresent());
    }
}

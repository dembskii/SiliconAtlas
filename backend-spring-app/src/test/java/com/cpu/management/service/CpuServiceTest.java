package com.cpu.management.service;

import com.cpu.management.domain.Cpu;
import com.cpu.management.domain.Manufacturer;
import com.cpu.management.dto.CpuCreateDTO;
import com.cpu.management.dto.CpuDTO;
import com.cpu.management.exception.CpuNotFoundException;
import com.cpu.management.exception.DuplicateCpuModelException;
import com.cpu.management.mapper.EntityMapper;
import com.cpu.management.repository.CpuBenchmarkRepository;
import com.cpu.management.repository.CpuRepository;
import com.cpu.management.repository.ManufacturerRepository;
import com.cpu.management.repository.TechnologyRepository;
import com.cpu.management.service.kafka.KafkaProducerService;
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
class CpuServiceTest {

    @Mock
    private CpuRepository cpuRepository;
    @Mock
    private CpuBenchmarkRepository benchmarkRepository;
    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Mock
    private TechnologyRepository technologyRepository;
    @Mock
    private EntityMapper entityMapper;
    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private CpuService cpuService;

    private Cpu sampleCpu;
    private CpuDTO sampleCpuDTO;
    private CpuCreateDTO sampleCreateDTO;
    private UUID sampleId;
    private UUID manufacturerId;
    private Manufacturer sampleManufacturer;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        manufacturerId = UUID.randomUUID();

        sampleManufacturer = new Manufacturer("AMD", "USA", 1969);
        sampleManufacturer.setId(manufacturerId);

        sampleCpu = new Cpu("Ryzen 9 7950X", 16, 32, 4.5);
        sampleCpu.setManufacturer(sampleManufacturer);

        sampleCpuDTO = CpuDTO.builder()
                .id(sampleId)
                .model("Ryzen 9 7950X")
                .cores(16)
                .threads(32)
                .frequencyGhz(4.5)
                .manufacturerName("AMD")
                .build();

        sampleCreateDTO = CpuCreateDTO.builder()
                .model("Ryzen 9 7950X")
                .cores(16)
                .threads(32)
                .frequencyGhz(4.5)
            .manufacturerId(manufacturerId)
                .build();
    }

    // =====================================================
    // addCpu Tests
    // =====================================================

    @Test
    void shouldAddCpuSuccessfully() {
        when(cpuRepository.findByModel("Ryzen 9 7950X")).thenReturn(Optional.empty());
        when(manufacturerRepository.findById(manufacturerId)).thenReturn(Optional.of(sampleManufacturer));
        when(entityMapper.toCpuEntity(sampleCreateDTO)).thenReturn(sampleCpu);
        when(cpuRepository.save(sampleCpu)).thenReturn(sampleCpu);
        when(entityMapper.toCpuDTO(sampleCpu)).thenReturn(sampleCpuDTO);

        CpuDTO result = cpuService.addCpu(sampleCreateDTO);

        assertNotNull(result);
        assertEquals("Ryzen 9 7950X", result.getModel());
        assertEquals(16, result.getCores());
        verify(cpuRepository).save(sampleCpu);
        verify(kafkaProducerService).publishCpuEvent(any());
    }

    @Test
    void shouldThrowDuplicateExceptionWhenModelExists() {
        when(cpuRepository.findByModel("Ryzen 9 7950X")).thenReturn(Optional.of(sampleCpu));

        assertThrows(DuplicateCpuModelException.class, () -> cpuService.addCpu(sampleCreateDTO));
        verify(cpuRepository, never()).save(any());
    }

    // =====================================================
    // getCpuById Tests
    // =====================================================

    @Test
    void shouldGetCpuByIdSuccessfully() {
        when(cpuRepository.findById(sampleId)).thenReturn(Optional.of(sampleCpu));
        when(entityMapper.toCpuDTO(sampleCpu)).thenReturn(sampleCpuDTO);

        Optional<CpuDTO> result = cpuService.getCpuById(sampleId);

        assertTrue(result.isPresent());
        assertEquals("Ryzen 9 7950X", result.get().getModel());
    }

    @Test
    void shouldReturnEmptyOptionalWhenCpuNotFound() {
        when(cpuRepository.findById(sampleId)).thenReturn(Optional.empty());

        Optional<CpuDTO> result = cpuService.getCpuById(sampleId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowCpuNotFoundExceptionForGetByIdOrThrow() {
        UUID nonExistentId = UUID.randomUUID();
        when(cpuRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(CpuNotFoundException.class, () -> cpuService.getCpuByIdOrThrow(nonExistentId));
    }

    // =====================================================
    // getAllCpus Tests
    // =====================================================

    @Test
    void shouldGetAllCpus() {
        Cpu cpu2 = new Cpu("Core i9-13900K", 24, 32, 3.0);
        List<Cpu> cpus = Arrays.asList(sampleCpu, cpu2);
        List<CpuDTO> cpuDTOs = Arrays.asList(sampleCpuDTO, CpuDTO.builder().model("Core i9-13900K").build());

        when(cpuRepository.findAll()).thenReturn(cpus);
        when(entityMapper.toCpuDTOList(anyList())).thenReturn(cpuDTOs);

        List<CpuDTO> result = cpuService.getAllCpus();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoCpus() {
        when(cpuRepository.findAll()).thenReturn(Collections.emptyList());
        when(entityMapper.toCpuDTOList(anyList())).thenReturn(Collections.emptyList());

        List<CpuDTO> result = cpuService.getAllCpus();

        assertTrue(result.isEmpty());
    }

    // =====================================================
    // deleteCpuById Tests
    // =====================================================

    @Test
    void shouldDeleteCpuSuccessfully() {
        when(cpuRepository.existsById(sampleId)).thenReturn(true);
        when(cpuRepository.findById(sampleId)).thenReturn(Optional.of(sampleCpu));

        cpuService.deleteCpuById(sampleId);

        verify(cpuRepository).deleteById(sampleId);
        verify(kafkaProducerService).publishCpuEvent(any());
    }

    @Test
    void shouldThrowCpuNotFoundExceptionOnDelete() {
        UUID nonExistentId = UUID.randomUUID();
        when(cpuRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(CpuNotFoundException.class, () -> cpuService.deleteCpuById(nonExistentId));
        verify(cpuRepository, never()).deleteById(any());
    }

    // =====================================================
    // updateCpu Tests
    // =====================================================

    @Test
    void shouldUpdateCpuSuccessfully() {
        CpuCreateDTO updateDTO = CpuCreateDTO.builder()
                .model("Ryzen 9 7950X3D")
                .cores(16)
                .threads(32)
                .frequencyGhz(4.2)
                .build();

        when(cpuRepository.findById(sampleId)).thenReturn(Optional.of(sampleCpu));
        when(cpuRepository.findByModel("Ryzen 9 7950X3D")).thenReturn(Optional.empty());
        when(cpuRepository.save(any(Cpu.class))).thenReturn(sampleCpu);
        when(entityMapper.toCpuDTO(any(Cpu.class))).thenReturn(sampleCpuDTO);

        CpuDTO result = cpuService.updateCpu(sampleId, updateDTO);

        assertNotNull(result);
        verify(cpuRepository).save(any(Cpu.class));
        verify(kafkaProducerService).publishCpuEvent(any());
    }

    @Test
    void shouldThrowCpuNotFoundExceptionOnUpdate() {
        UUID nonExistentId = UUID.randomUUID();
        when(cpuRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(CpuNotFoundException.class, () -> cpuService.updateCpu(nonExistentId, sampleCreateDTO));
    }

    // =====================================================
    // Entity methods Tests
    // =====================================================

    @Test
    void shouldAddCpuEntitySuccessfully() {
        when(cpuRepository.save(sampleCpu)).thenReturn(sampleCpu);

        Cpu result = cpuService.addCpuEntity(sampleCpu);

        assertNotNull(result);
        assertEquals("Ryzen 9 7950X", result.getModel());
    }

    @Test
    void shouldGetCpuEntityById() {
        when(cpuRepository.findById(sampleId)).thenReturn(Optional.of(sampleCpu));

        Optional<Cpu> result = cpuService.getCpuEntityById(sampleId);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldGetAllCpuEntities() {
        when(cpuRepository.findAll()).thenReturn(List.of(sampleCpu));

        List<Cpu> result = cpuService.getAllCpuEntities();

        assertEquals(1, result.size());
    }

    // =====================================================
    // createCpuWithManufacturerAndTechnologiesEntity Tests
    // =====================================================

    @Test
    void shouldCreateCpuWithManufacturerEntity() {
        UUID manufacturerId = UUID.randomUUID();
        Manufacturer manufacturer = new Manufacturer("AMD", "USA", 1969);
        when(manufacturerRepository.findById(manufacturerId)).thenReturn(Optional.of(manufacturer));
        when(cpuRepository.save(any(Cpu.class))).thenReturn(sampleCpu);

        Cpu result = cpuService.createCpuWithManufacturerAndTechnologiesEntity(sampleCpu, manufacturerId, null);

        assertNotNull(result);
        verify(cpuRepository).save(any(Cpu.class));
        verify(kafkaProducerService).publishCpuEvent(any());
    }

    @Test
    void shouldThrowWhenManufacturerNotFound() {
        UUID badId = UUID.randomUUID();
        when(manufacturerRepository.findById(badId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> cpuService.createCpuWithManufacturerAndTechnologiesEntity(sampleCpu, badId, null));
    }
}

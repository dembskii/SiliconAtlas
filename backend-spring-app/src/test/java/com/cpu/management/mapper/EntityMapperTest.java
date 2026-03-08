package com.cpu.management.mapper;

import com.cpu.management.domain.*;
import com.cpu.management.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EntityMapperTest {

    private EntityMapper entityMapper;

    @BeforeEach
    void setUp() {
        entityMapper = new EntityMapper();
    }

    // =====================================================
    // CPU Mapper Tests
    // =====================================================

    @Test
    void shouldMapCpuToDTO() {
        Manufacturer manufacturer = new Manufacturer("AMD", "USA", 1969);
        Technology smt = new Technology("SMT", "Simultaneous Multithreading", 2017);

        Cpu cpu = new Cpu("Ryzen 9 7950X", 16, 32, 4.5);
        cpu.setManufacturer(manufacturer);
        cpu.setTechnologies(List.of(smt));

        CpuDTO dto = entityMapper.toCpuDTO(cpu);

        assertNotNull(dto);
        assertEquals("Ryzen 9 7950X", dto.getModel());
        assertEquals(16, dto.getCores());
        assertEquals(32, dto.getThreads());
        assertEquals(4.5, dto.getFrequencyGhz());
        assertEquals("AMD", dto.getManufacturerName());
        assertEquals(1, dto.getTechnologyNames().size());
        assertEquals("SMT", dto.getTechnologyNames().get(0));
    }

    @Test
    void shouldMapCpuToDTOWithNullManufacturer() {
        Cpu cpu = new Cpu("Test CPU", 8, 16, 3.5);
        cpu.setTechnologies(Collections.emptyList());

        CpuDTO dto = entityMapper.toCpuDTO(cpu);

        assertNotNull(dto);
        assertNull(dto.getManufacturerName());
        assertNull(dto.getManufacturerId());
    }

    @Test
    void shouldReturnNullForNullCpu() {
        assertNull(entityMapper.toCpuDTO(null));
    }

    @Test
    void shouldMapCpuCreateDTOToEntity() {
        CpuCreateDTO dto = CpuCreateDTO.builder()
                .model("Core i9-13900K")
                .cores(24)
                .threads(32)
                .frequencyGhz(3.0)
                .build();

        Cpu cpu = entityMapper.toCpuEntity(dto);

        assertNotNull(cpu);
        assertEquals("Core i9-13900K", cpu.getModel());
        assertEquals(24, cpu.getCores());
        assertEquals(32, cpu.getThreads());
        assertEquals(3.0, cpu.getFrequencyGhz());
    }

    @Test
    void shouldReturnNullForNullCpuCreateDTO() {
        assertNull(entityMapper.toCpuEntity(null));
    }

    @Test
    void shouldMapCpuListToDTOList() {
        Cpu cpu1 = new Cpu("Ryzen 5 5600X", 6, 12, 3.7);
        cpu1.setTechnologies(Collections.emptyList());
        Cpu cpu2 = new Cpu("Ryzen 7 5800X", 8, 16, 3.8);
        cpu2.setTechnologies(Collections.emptyList());

        List<CpuDTO> dtos = entityMapper.toCpuDTOList(Arrays.asList(cpu1, cpu2));

        assertEquals(2, dtos.size());
        assertEquals("Ryzen 5 5600X", dtos.get(0).getModel());
        assertEquals("Ryzen 7 5800X", dtos.get(1).getModel());
    }

    @Test
    void shouldReturnEmptyListForNullCpuList() {
        List<CpuDTO> dtos = entityMapper.toCpuDTOList(null);
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    // =====================================================
    // Manufacturer Mapper Tests
    // =====================================================

    @Test
    void shouldMapManufacturerToDTO() {
        Manufacturer manufacturer = new Manufacturer("Intel", "USA", 1968);
        manufacturer.setCpus(new ArrayList<>());

        ManufacturerDTO dto = entityMapper.toManufacturerDTO(manufacturer);

        assertNotNull(dto);
        assertEquals("Intel", dto.getName());
        assertEquals("USA", dto.getCountry());
        assertEquals(1968, dto.getFoundedYear());
        assertEquals(0, dto.getCpuCount());
    }

    @Test
    void shouldReturnNullForNullManufacturer() {
        assertNull(entityMapper.toManufacturerDTO(null));
    }

    @Test
    void shouldMapManufacturerCreateDTOToEntity() {
        ManufacturerCreateDTO dto = ManufacturerCreateDTO.builder()
                .name("AMD")
                .country("USA")
                .foundedYear(1969)
                .build();

        Manufacturer manufacturer = entityMapper.toManufacturerEntity(dto);

        assertNotNull(manufacturer);
        assertEquals("AMD", manufacturer.getName());
        assertEquals("USA", manufacturer.getCountry());
        assertEquals(1969, manufacturer.getFoundedYear());
    }

    @Test
    void shouldReturnNullForNullManufacturerCreateDTO() {
        assertNull(entityMapper.toManufacturerEntity(null));
    }

    @Test
    void shouldMapManufacturerListToDTOList() {
        Manufacturer m1 = new Manufacturer("Intel", "USA", 1968);
        m1.setCpus(new ArrayList<>());
        Manufacturer m2 = new Manufacturer("AMD", "USA", 1969);
        m2.setCpus(new ArrayList<>());

        List<ManufacturerDTO> dtos = entityMapper.toManufacturerDTOList(Arrays.asList(m1, m2));

        assertEquals(2, dtos.size());
    }

    @Test
    void shouldReturnEmptyListForNullManufacturerList() {
        assertTrue(entityMapper.toManufacturerDTOList(null).isEmpty());
    }

    // =====================================================
    // Technology Mapper Tests
    // =====================================================

    @Test
    void shouldMapTechnologyToDTO() {
        Technology technology = new Technology("SMT", "Simultaneous Multithreading", 2017);
        technology.setCpus(new ArrayList<>());

        TechnologyDTO dto = entityMapper.toTechnologyDTO(technology);

        assertNotNull(dto);
        assertEquals("SMT", dto.getName());
        assertEquals("Simultaneous Multithreading", dto.getDescription());
        assertEquals(2017, dto.getReleaseYear());
        assertEquals(0, dto.getCpuCount());
    }

    @Test
    void shouldReturnNullForNullTechnology() {
        assertNull(entityMapper.toTechnologyDTO(null));
    }

    @Test
    void shouldMapTechnologyCreateDTOToEntity() {
        TechnologyCreateDTO dto = TechnologyCreateDTO.builder()
                .name("PCIe 5.0")
                .description("Latest PCI Express standard")
                .releaseYear(2022)
                .build();

        Technology technology = entityMapper.toTechnologyEntity(dto);

        assertNotNull(technology);
        assertEquals("PCIe 5.0", technology.getName());
        assertEquals("Latest PCI Express standard", technology.getDescription());
        assertEquals(2022, technology.getReleaseYear());
    }

    @Test
    void shouldReturnNullForNullTechnologyCreateDTO() {
        assertNull(entityMapper.toTechnologyEntity(null));
    }

    // =====================================================
    // CpuBenchmark Mapper Tests
    // =====================================================

    @Test
    void shouldMapCpuBenchmarkToDTO() {
        Cpu cpu = new Cpu("Ryzen 9 7950X", 16, 32, 4.5);
        CpuBenchmark benchmark = new CpuBenchmark(2100, 30000, 45000, 38000.0, "2024-01-15");
        benchmark.setCpu(cpu);

        CpuBenchmarkDTO dto = entityMapper.toCpuBenchmarkDTO(benchmark);

        assertNotNull(dto);
        assertEquals(2100, dto.getSingleCoreScore());
        assertEquals(30000, dto.getMultiCoreScore());
        assertEquals(45000, dto.getPassmarkScore());
        assertEquals(38000.0, dto.getCinebenchR23());
        assertEquals("2024-01-15", dto.getTestDate());
        assertEquals("Ryzen 9 7950X", dto.getCpuModel());
    }

    @Test
    void shouldMapBenchmarkToDTOWithNullCpu() {
        CpuBenchmark benchmark = new CpuBenchmark(2100, 30000, 45000, 38000.0, "2024-01-15");

        CpuBenchmarkDTO dto = entityMapper.toCpuBenchmarkDTO(benchmark);

        assertNotNull(dto);
        assertNull(dto.getCpuId());
        assertNull(dto.getCpuModel());
    }

    @Test
    void shouldReturnNullForNullCpuBenchmark() {
        assertNull(entityMapper.toCpuBenchmarkDTO(null));
    }

    @Test
    void shouldMapCpuBenchmarkCreateDTOToEntity() {
        CpuBenchmarkCreateDTO dto = CpuBenchmarkCreateDTO.builder()
                .singleCoreScore(2100)
                .multiCoreScore(30000)
                .passmarkScore(45000)
                .cinebenchR23(38000.0)
                .testDate("2024-01-15")
                .build();

        CpuBenchmark benchmark = entityMapper.toCpuBenchmarkEntity(dto);

        assertNotNull(benchmark);
        assertEquals(2100, benchmark.getSingleCoreScore());
        assertEquals(30000, benchmark.getMultiCoreScore());
        assertEquals(45000, benchmark.getPassmarkScore());
    }

    @Test
    void shouldReturnNullForNullBenchmarkCreateDTO() {
        assertNull(entityMapper.toCpuBenchmarkEntity(null));
    }

    // =====================================================
    // CpuSpecification Mapper Tests
    // =====================================================

    @Test
    void shouldMapCpuSpecificationToDTO() {
        CpuSpecification spec = new CpuSpecification(64, 1024, 64, 170, "AM5");

        CpuSpecificationDTO dto = entityMapper.toCpuSpecificationDTO(spec);

        assertNotNull(dto);
        assertEquals(64, dto.getCacheL1KB());
        assertEquals(1024, dto.getCacheL2KB());
        assertEquals(64, dto.getCacheL3MB());
        assertEquals(170, dto.getTdpWatts());
        assertEquals("AM5", dto.getSocketType());
    }

    @Test
    void shouldReturnNullForNullCpuSpecification() {
        assertNull(entityMapper.toCpuSpecificationDTO(null));
    }

    @Test
    void shouldMapCpuSpecificationCreateDTOToEntity() {
        CpuSpecificationCreateDTO dto = CpuSpecificationCreateDTO.builder()
                .cacheL1KB(80)
                .cacheL2KB(2048)
                .cacheL3MB(36)
                .tdpWatts(125)
                .socketType("LGA1700")
                .build();

        CpuSpecification spec = entityMapper.toCpuSpecificationEntity(dto);

        assertNotNull(spec);
        assertEquals(80, spec.getCacheL1KB());
        assertEquals(2048, spec.getCacheL2KB());
        assertEquals(36, spec.getCacheL3MB());
        assertEquals(125, spec.getTdpWatts());
        assertEquals("LGA1700", spec.getSocketType());
    }

    @Test
    void shouldReturnNullForNullSpecificationCreateDTO() {
        assertNull(entityMapper.toCpuSpecificationEntity(null));
    }
}

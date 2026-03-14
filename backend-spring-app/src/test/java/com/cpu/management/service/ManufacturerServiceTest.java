package com.cpu.management.service;

import com.cpu.management.domain.Cpu;
import com.cpu.management.domain.Manufacturer;
import com.cpu.management.dto.ManufacturerCreateDTO;
import com.cpu.management.dto.ManufacturerDTO;
import com.cpu.management.mapper.EntityMapper;
import com.cpu.management.repository.ManufacturerRepository;
import com.cpu.management.service.kafka.KafkaProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManufacturerServiceTest {

    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Mock
    private EntityMapper entityMapper;
    @Mock
    private KafkaProducerService kafkaProducerService;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private Cache cpusCache;
    @Mock
    private Cache allCpusCache;

    @InjectMocks
    private ManufacturerService manufacturerService;

    private Manufacturer sampleManufacturer;
    private ManufacturerDTO sampleManufacturerDTO;
    private ManufacturerCreateDTO sampleCreateDTO;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();

        sampleManufacturer = new Manufacturer("Intel", "USA", 1968);
        sampleManufacturer.setCpus(new ArrayList<>());

        sampleManufacturerDTO = ManufacturerDTO.builder()
                .id(sampleId)
                .name("Intel")
                .country("USA")
                .foundedYear(1968)
                .cpuCount(0)
                .build();

        sampleCreateDTO = ManufacturerCreateDTO.builder()
                .name("Intel")
                .country("USA")
                .foundedYear(1968)
                .build();

        lenient().when(cacheManager.getCache("cpus")).thenReturn(cpusCache);
        lenient().when(cacheManager.getCache("allCpus")).thenReturn(allCpusCache);
    }

    @Test
    void shouldAddManufacturer() {
        when(entityMapper.toManufacturerEntity(sampleCreateDTO)).thenReturn(sampleManufacturer);
        when(manufacturerRepository.save(sampleManufacturer)).thenReturn(sampleManufacturer);
        when(entityMapper.toManufacturerDTO(sampleManufacturer)).thenReturn(sampleManufacturerDTO);

        ManufacturerDTO result = manufacturerService.addManufacturer(sampleCreateDTO);

        assertNotNull(result);
        assertEquals("Intel", result.getName());
        assertEquals("USA", result.getCountry());
        verify(manufacturerRepository).save(sampleManufacturer);
        verify(kafkaProducerService).publishManufacturerEvent(any());
    }

    @Test
    void shouldGetManufacturerById() {
        when(manufacturerRepository.findById(sampleId)).thenReturn(Optional.of(sampleManufacturer));
        when(entityMapper.toManufacturerDTO(sampleManufacturer)).thenReturn(sampleManufacturerDTO);

        Optional<ManufacturerDTO> result = manufacturerService.getManufacturerById(sampleId);

        assertTrue(result.isPresent());
        assertEquals("Intel", result.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenManufacturerNotFound() {
        when(manufacturerRepository.findById(sampleId)).thenReturn(Optional.empty());

        Optional<ManufacturerDTO> result = manufacturerService.getManufacturerById(sampleId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGetAllManufacturers() {
        Manufacturer m2 = new Manufacturer("AMD", "USA", 1969);
        m2.setCpus(new ArrayList<>());
        List<Manufacturer> manufacturers = Arrays.asList(sampleManufacturer, m2);
        List<ManufacturerDTO> dtos = Arrays.asList(sampleManufacturerDTO,
                ManufacturerDTO.builder().name("AMD").build());

        when(manufacturerRepository.findAll()).thenReturn(manufacturers);
        when(entityMapper.toManufacturerDTOList(anyList())).thenReturn(dtos);

        List<ManufacturerDTO> result = manufacturerService.getAllManufacturers();

        assertEquals(2, result.size());
    }

    @Test
    void shouldDeleteManufacturer() {
        when(manufacturerRepository.findById(sampleId)).thenReturn(Optional.of(sampleManufacturer));

        manufacturerService.deleteManufacturerById(sampleId);

        verify(manufacturerRepository).deleteById(sampleId);
        verify(allCpusCache).evict("allCpus");
        verify(kafkaProducerService).publishManufacturerEvent(any());
    }

    @Test
    void shouldEvictOnlyRemovedCpuCacheEntriesWhenDeletingManufacturer() {
        UUID removedCpuId = UUID.randomUUID();
        Cpu removedCpu = new Cpu("Ryzen 5 7600", 6, 12, 3.8);
        removedCpu.setId(removedCpuId);
        sampleManufacturer.setCpus(List.of(removedCpu));

        when(manufacturerRepository.findById(sampleId)).thenReturn(Optional.of(sampleManufacturer));

        manufacturerService.deleteManufacturerById(sampleId);

        verify(cpusCache).evict(removedCpuId);
        verify(allCpusCache).evict("allCpus");
    }

    @Test
    void shouldNotPublishEventWhenDeletingNonExistentManufacturer() {
        when(manufacturerRepository.findById(sampleId)).thenReturn(Optional.empty());

        manufacturerService.deleteManufacturerById(sampleId);

        verify(manufacturerRepository, never()).deleteById(any());
        verify(kafkaProducerService, never()).publishManufacturerEvent(any());
    }

    @Test
    void shouldUpdateManufacturer() {
        ManufacturerCreateDTO updateDTO = ManufacturerCreateDTO.builder()
                .name("Intel Corp")
                .country("United States")
                .foundedYear(1968)
                .build();

        when(manufacturerRepository.findById(sampleId)).thenReturn(Optional.of(sampleManufacturer));
        when(manufacturerRepository.save(any(Manufacturer.class))).thenReturn(sampleManufacturer);
        when(entityMapper.toManufacturerDTO(any(Manufacturer.class))).thenReturn(sampleManufacturerDTO);

        ManufacturerDTO result = manufacturerService.updateManufacturer(sampleId, updateDTO);

        assertNotNull(result);
        verify(manufacturerRepository).save(any(Manufacturer.class));
        verify(kafkaProducerService).publishManufacturerEvent(any());
    }

    @Test
    void shouldReturnNullWhenUpdatingNonExistentManufacturer() {
        when(manufacturerRepository.findById(sampleId)).thenReturn(Optional.empty());

        ManufacturerDTO result = manufacturerService.updateManufacturer(sampleId, sampleCreateDTO);

        assertNull(result);
        verify(manufacturerRepository, never()).save(any());
    }

    @Test
    void shouldAddManufacturerEntity() {
        when(manufacturerRepository.save(sampleManufacturer)).thenReturn(sampleManufacturer);

        Manufacturer result = manufacturerService.addManufacturerEntity(sampleManufacturer);

        assertNotNull(result);
        assertEquals("Intel", result.getName());
        verify(kafkaProducerService).publishManufacturerEvent(any());
    }

    @Test
    void shouldGetManufacturerEntityById() {
        when(manufacturerRepository.findById(sampleId)).thenReturn(Optional.of(sampleManufacturer));

        Optional<Manufacturer> result = manufacturerService.getManufacturerEntityById(sampleId);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldGetAllManufacturerEntities() {
        when(manufacturerRepository.findAll()).thenReturn(List.of(sampleManufacturer));

        List<Manufacturer> result = manufacturerService.getAllManufacturerEntities();

        assertEquals(1, result.size());
    }
}

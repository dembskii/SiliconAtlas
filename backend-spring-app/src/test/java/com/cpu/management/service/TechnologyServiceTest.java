package com.cpu.management.service;

import com.cpu.management.domain.Cpu;
import com.cpu.management.domain.Technology;
import com.cpu.management.dto.TechnologyCreateDTO;
import com.cpu.management.dto.TechnologyDTO;
import com.cpu.management.mapper.EntityMapper;
import com.cpu.management.repository.TechnologyRepository;
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
class TechnologyServiceTest {

    @Mock
    private TechnologyRepository technologyRepository;
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
    private TechnologyService technologyService;

    private Technology sampleTechnology;
    private TechnologyDTO sampleTechnologyDTO;
    private TechnologyCreateDTO sampleCreateDTO;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();

        sampleTechnology = new Technology("SMT", "Simultaneous Multithreading", 2017);
        sampleTechnology.setCpus(new ArrayList<>());

        sampleTechnologyDTO = TechnologyDTO.builder()
                .id(sampleId)
                .name("SMT")
                .description("Simultaneous Multithreading")
                .releaseYear(2017)
                .cpuCount(0)
                .build();

        sampleCreateDTO = TechnologyCreateDTO.builder()
                .name("SMT")
                .description("Simultaneous Multithreading")
                .releaseYear(2017)
                .build();

        lenient().when(cacheManager.getCache("cpus")).thenReturn(cpusCache);
        lenient().when(cacheManager.getCache("allCpus")).thenReturn(allCpusCache);
    }

    @Test
    void shouldAddTechnology() {
        when(entityMapper.toTechnologyEntity(sampleCreateDTO)).thenReturn(sampleTechnology);
        when(technologyRepository.save(sampleTechnology)).thenReturn(sampleTechnology);
        when(entityMapper.toTechnologyDTO(sampleTechnology)).thenReturn(sampleTechnologyDTO);

        TechnologyDTO result = technologyService.addTechnology(sampleCreateDTO);

        assertNotNull(result);
        assertEquals("SMT", result.getName());
        verify(technologyRepository).save(sampleTechnology);
        verify(kafkaProducerService).publishTechnologyEvent(any());
    }

    @Test
    void shouldGetTechnologyById() {
        when(technologyRepository.findById(sampleId)).thenReturn(Optional.of(sampleTechnology));
        when(entityMapper.toTechnologyDTO(sampleTechnology)).thenReturn(sampleTechnologyDTO);

        Optional<TechnologyDTO> result = technologyService.getTechnologyById(sampleId);

        assertTrue(result.isPresent());
        assertEquals("SMT", result.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenTechnologyNotFound() {
        when(technologyRepository.findById(sampleId)).thenReturn(Optional.empty());

        Optional<TechnologyDTO> result = technologyService.getTechnologyById(sampleId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGetAllTechnologies() {
        Technology t2 = new Technology("DDR5", "DDR5 Memory", 2020);
        t2.setCpus(new ArrayList<>());
        List<Technology> technologies = Arrays.asList(sampleTechnology, t2);
        List<TechnologyDTO> dtos = Arrays.asList(sampleTechnologyDTO,
                TechnologyDTO.builder().name("DDR5").build());

        when(technologyRepository.findAll()).thenReturn(technologies);
        when(entityMapper.toTechnologyDTOList(anyList())).thenReturn(dtos);

        List<TechnologyDTO> result = technologyService.getAllTechnologies();

        assertEquals(2, result.size());
    }

    @Test
    void shouldDeleteTechnology() {
        when(technologyRepository.findById(sampleId)).thenReturn(Optional.of(sampleTechnology));

        technologyService.deleteTechnologyById(sampleId);

        verify(technologyRepository).deleteById(sampleId);
        verify(allCpusCache).evict("allCpus");
        verify(kafkaProducerService).publishTechnologyEvent(any());
    }

    @Test
    void shouldEvictOnlyImpactedCpuCacheEntriesWhenDeletingTechnology() {
        UUID impactedCpuId = UUID.randomUUID();
        Cpu impactedCpu = new Cpu("Core i7-12700K", 12, 20, 3.6);
        impactedCpu.setId(impactedCpuId);
        sampleTechnology.setCpus(List.of(impactedCpu));

        when(technologyRepository.findById(sampleId)).thenReturn(Optional.of(sampleTechnology));

        technologyService.deleteTechnologyById(sampleId);

        verify(cpusCache).evict(impactedCpuId);
        verify(allCpusCache).evict("allCpus");
    }

    @Test
    void shouldNotPublishEventWhenDeletingNonExistentTechnology() {
        when(technologyRepository.findById(sampleId)).thenReturn(Optional.empty());

        technologyService.deleteTechnologyById(sampleId);

        verify(technologyRepository, never()).deleteById(any());
        verify(kafkaProducerService, never()).publishTechnologyEvent(any());
    }

    @Test
    void shouldUpdateTechnology() {
        TechnologyCreateDTO updateDTO = TechnologyCreateDTO.builder()
                .name("SMT 2.0")
                .description("Updated SMT")
                .releaseYear(2023)
                .build();

        when(technologyRepository.findById(sampleId)).thenReturn(Optional.of(sampleTechnology));
        when(technologyRepository.save(any(Technology.class))).thenReturn(sampleTechnology);
        when(entityMapper.toTechnologyDTO(any(Technology.class))).thenReturn(sampleTechnologyDTO);

        TechnologyDTO result = technologyService.updateTechnology(sampleId, updateDTO);

        assertNotNull(result);
        verify(technologyRepository).save(any(Technology.class));
        verify(kafkaProducerService).publishTechnologyEvent(any());
    }

    @Test
    void shouldReturnNullWhenUpdatingNonExistentTechnology() {
        when(technologyRepository.findById(sampleId)).thenReturn(Optional.empty());

        TechnologyDTO result = technologyService.updateTechnology(sampleId, sampleCreateDTO);

        assertNull(result);
        verify(technologyRepository, never()).save(any());
    }

    @Test
    void shouldAddTechnologyEntity() {
        when(technologyRepository.save(sampleTechnology)).thenReturn(sampleTechnology);

        Technology result = technologyService.addTechnologyEntity(sampleTechnology);

        assertNotNull(result);
        assertEquals("SMT", result.getName());
        verify(kafkaProducerService).publishTechnologyEvent(any());
    }

    @Test
    void shouldGetTechnologyEntityById() {
        when(technologyRepository.findById(sampleId)).thenReturn(Optional.of(sampleTechnology));

        Optional<Technology> result = technologyService.getTechnologyEntityById(sampleId);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldGetAllTechnologyEntities() {
        when(technologyRepository.findAll()).thenReturn(List.of(sampleTechnology));

        List<Technology> result = technologyService.getAllTechnologyEntities();

        assertEquals(1, result.size());
    }
}

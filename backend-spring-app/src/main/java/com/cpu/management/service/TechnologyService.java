package com.cpu.management.service;

import com.cpu.management.domain.Technology;
import com.cpu.management.dto.TechnologyCreateDTO;
import com.cpu.management.dto.TechnologyDTO;
import com.cpu.management.dto.event.TechnologyEventDTO;
import com.cpu.management.mapper.EntityMapper;
import com.cpu.management.repository.TechnologyRepository;
import com.cpu.management.service.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TechnologyService {

    private final TechnologyRepository technologyRepository;
    private final EntityMapper entityMapper;
    private final KafkaProducerService kafkaProducerService;

    // =====================================================
    // METODY DTO - dla REST API
    // =====================================================

    @CacheEvict(value = {"allTechnologies", "technologies"}, allEntries = true)
    public TechnologyDTO addTechnology(TechnologyCreateDTO technologyCreateDTO) {
        Technology technology = entityMapper.toTechnologyEntity(technologyCreateDTO);
        Technology savedTechnology = technologyRepository.save(technology);
        TechnologyDTO result = entityMapper.toTechnologyDTO(savedTechnology);
        
        // Publikuj event do Kafki
        TechnologyEventDTO event = TechnologyEventDTO.builder()
                .eventId(UUID.randomUUID())
                .eventType("CREATE")
                .technologyId(savedTechnology.getId())
                .technologyName(savedTechnology.getName())
                .description(savedTechnology.getDescription())
                .releaseYear(savedTechnology.getReleaseYear())
                .timestamp(LocalDateTime.now())
                .userId("system")
                .details("Technology successfully created: " + savedTechnology.getName())
                .build();
        
        kafkaProducerService.publishTechnologyEvent(event);
        
        return result;
    }

    @Cacheable(value = "technologies", key = "#id")
    public Optional<TechnologyDTO> getTechnologyById(UUID id) {
        return technologyRepository.findById(id).map(entityMapper::toTechnologyDTO);
    }

    @Cacheable(value = "allTechnologies", key = "'allTechnologies'")
    public List<TechnologyDTO> getAllTechnologies() {
        List<Technology> technologies = new ArrayList<>();
        technologyRepository.findAll().forEach(technologies::add);
        return entityMapper.toTechnologyDTOList(technologies);
    }

    @CacheEvict(value = {"allTechnologies", "technologies"}, key = "#id")
    public void deleteTechnologyById(UUID id) {
        Optional<Technology> technologyOpt = technologyRepository.findById(id);
        if (technologyOpt.isPresent()) {
            Technology technology = technologyOpt.get();
            technologyRepository.deleteById(id);
            
            // Publikuj event do Kafki
            TechnologyEventDTO event = TechnologyEventDTO.builder()
                    .eventId(UUID.randomUUID())
                    .eventType("DELETE")
                    .technologyId(technology.getId())
                    .technologyName(technology.getName())
                    .description(technology.getDescription())
                    .releaseYear(technology.getReleaseYear())
                    .timestamp(LocalDateTime.now())
                    .userId("system")
                    .details("Technology successfully deleted: " + technology.getName())
                    .build();
            
            kafkaProducerService.publishTechnologyEvent(event);
        }
    }

    @CacheEvict(value = {"allTechnologies", "technologies"}, allEntries = true)
    public TechnologyDTO updateTechnology(UUID id, TechnologyCreateDTO technologyCreateDTO) {
        Optional<Technology> technologyOpt = technologyRepository.findById(id);
        if (technologyOpt.isPresent()) {
            Technology existingTechnology = technologyOpt.get();
            if (technologyCreateDTO.getName() != null) {
                existingTechnology.setName(technologyCreateDTO.getName());
            }
            if (technologyCreateDTO.getDescription() != null) {
                existingTechnology.setDescription(technologyCreateDTO.getDescription());
            }
            if (technologyCreateDTO.getReleaseYear() > 0) {
                existingTechnology.setReleaseYear(technologyCreateDTO.getReleaseYear());
            }
            Technology savedTechnology = technologyRepository.save(existingTechnology);
            TechnologyDTO result = entityMapper.toTechnologyDTO(savedTechnology);
            
            // Publikuj event do Kafki
            TechnologyEventDTO event = TechnologyEventDTO.builder()
                    .eventId(UUID.randomUUID())
                    .eventType("UPDATE")
                    .technologyId(savedTechnology.getId())
                    .technologyName(savedTechnology.getName())
                    .description(savedTechnology.getDescription())
                    .releaseYear(savedTechnology.getReleaseYear())
                    .timestamp(LocalDateTime.now())
                    .userId("system")
                    .details("Technology successfully updated: " + savedTechnology.getName())
                    .build();
            
            kafkaProducerService.publishTechnologyEvent(event);
            
            return result;
        }
        return null;
    }

    // =====================================================
    // METODY WEWNĘTRZNE (encje) - dla AdminController/Thymeleaf
    // =====================================================

    public Technology addTechnologyEntity(Technology technology) {
        return technologyRepository.save(technology);
    }

    public Optional<Technology> getTechnologyEntityById(UUID id) {
        return technologyRepository.findById(id);
    }

    public List<Technology> getAllTechnologyEntities() {
        List<Technology> technologies = new ArrayList<>();
        technologyRepository.findAll().forEach(technologies::add);
        return technologies;
    }
}
 
package com.cpu.management.service;

import com.cpu.management.domain.Manufacturer;
import com.cpu.management.dto.ManufacturerCreateDTO;
import com.cpu.management.dto.ManufacturerDTO;
import com.cpu.management.dto.event.ManufacturerEventDTO;
import com.cpu.management.mapper.EntityMapper;
import com.cpu.management.repository.ManufacturerRepository;
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
public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    private final EntityMapper entityMapper;
    private final KafkaProducerService kafkaProducerService;

    // =====================================================
    // METODY DTO - dla REST API
    // =====================================================

    @CacheEvict(value = {"allManufacturers", "manufacturers"}, allEntries = true)
    public ManufacturerDTO addManufacturer(ManufacturerCreateDTO manufacturerCreateDTO) {
        Manufacturer manufacturer = entityMapper.toManufacturerEntity(manufacturerCreateDTO);
        Manufacturer savedManufacturer = manufacturerRepository.save(manufacturer);
        ManufacturerDTO result = entityMapper.toManufacturerDTO(savedManufacturer);
        
        // Publikuj event do Kafki
        ManufacturerEventDTO event = ManufacturerEventDTO.builder()
                .eventId(UUID.randomUUID())
                .eventType("CREATE")
                .manufacturerId(savedManufacturer.getId())
                .manufacturerName(savedManufacturer.getName())
                .country(savedManufacturer.getCountry())
                .foundedYear(savedManufacturer.getFoundedYear())
                .timestamp(LocalDateTime.now())
                .userId("system")
                .details("Manufacturer successfully created: " + savedManufacturer.getName())
                .build();
        
        kafkaProducerService.publishManufacturerEvent(event);
        
        return result;
    }

    @Cacheable(value = "manufacturers", key = "#id")
    public Optional<ManufacturerDTO> getManufacturerById(UUID id) {
        return manufacturerRepository.findById(id).map(entityMapper::toManufacturerDTO);
    }

    @Cacheable(value = "allManufacturers", key = "'allManufacturers'")
    public List<ManufacturerDTO> getAllManufacturers() {
        List<Manufacturer> manufacturers = new ArrayList<>();
        manufacturerRepository.findAll().forEach(manufacturers::add);
        return entityMapper.toManufacturerDTOList(manufacturers);
    }

    @CacheEvict(value = {"allManufacturers", "manufacturers"}, key = "#id")
    public void deleteManufacturerById(UUID id) {
        Optional<Manufacturer> manufacturerOpt = manufacturerRepository.findById(id);
        if (manufacturerOpt.isPresent()) {
            Manufacturer manufacturer = manufacturerOpt.get();
            manufacturerRepository.deleteById(id);
            
            // Publikuj event do Kafki
            ManufacturerEventDTO event = ManufacturerEventDTO.builder()
                    .eventId(UUID.randomUUID())
                    .eventType("DELETE")
                    .manufacturerId(manufacturer.getId())
                    .manufacturerName(manufacturer.getName())
                    .country(manufacturer.getCountry())
                    .foundedYear(manufacturer.getFoundedYear())
                    .timestamp(LocalDateTime.now())
                    .userId("system")
                    .details("Manufacturer successfully deleted: " + manufacturer.getName())
                    .build();
            
            kafkaProducerService.publishManufacturerEvent(event);
        }
    }

    @CacheEvict(value = {"allManufacturers", "manufacturers"}, allEntries = true)
    public ManufacturerDTO updateManufacturer(UUID id, ManufacturerCreateDTO manufacturerCreateDTO) {
        Optional<Manufacturer> manufacturerOpt = manufacturerRepository.findById(id);
        if (manufacturerOpt.isPresent()) {
            Manufacturer existingManufacturer = manufacturerOpt.get();
            if (manufacturerCreateDTO.getName() != null) {
                existingManufacturer.setName(manufacturerCreateDTO.getName());
            }
            if (manufacturerCreateDTO.getCountry() != null) {
                existingManufacturer.setCountry(manufacturerCreateDTO.getCountry());
            }
            if (manufacturerCreateDTO.getFoundedYear() > 0) {
                existingManufacturer.setFoundedYear(manufacturerCreateDTO.getFoundedYear());
            }
            Manufacturer savedManufacturer = manufacturerRepository.save(existingManufacturer);
            ManufacturerDTO result = entityMapper.toManufacturerDTO(savedManufacturer);
            
            // Publikuj event do Kafki
            ManufacturerEventDTO event = ManufacturerEventDTO.builder()
                    .eventId(UUID.randomUUID())
                    .eventType("UPDATE")
                    .manufacturerId(savedManufacturer.getId())
                    .manufacturerName(savedManufacturer.getName())
                    .country(savedManufacturer.getCountry())
                    .foundedYear(savedManufacturer.getFoundedYear())
                    .timestamp(LocalDateTime.now())
                    .userId("system")
                    .details("Manufacturer successfully updated: " + savedManufacturer.getName())
                    .build();
            
            kafkaProducerService.publishManufacturerEvent(event);
            
            return result;
        }
        return null;
    }

    // =====================================================
    // METODY WEWNĘTRZNE (encje) - dla AdminController/Thymeleaf
    // =====================================================

    public Manufacturer addManufacturerEntity(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    public Optional<Manufacturer> getManufacturerEntityById(UUID id) {
        return manufacturerRepository.findById(id);
    }

    public List<Manufacturer> getAllManufacturerEntities() {
        List<Manufacturer> manufacturers = new ArrayList<>();
        manufacturerRepository.findAll().forEach(manufacturers::add);
        return manufacturers;
    }
}

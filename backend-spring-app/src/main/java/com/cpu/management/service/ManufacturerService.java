package com.cpu.management.service;

import com.cpu.management.domain.Manufacturer;
import com.cpu.management.dto.ManufacturerCreateDTO;
import com.cpu.management.dto.ManufacturerDTO;
import com.cpu.management.mapper.EntityMapper;
import com.cpu.management.repository.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // =====================================================
    // METODY DTO - dla REST API
    // =====================================================

    @CacheEvict(value = {"allManufacturers", "manufacturers"}, allEntries = true)
    public ManufacturerDTO addManufacturer(ManufacturerCreateDTO manufacturerCreateDTO) {
        Manufacturer manufacturer = entityMapper.toManufacturerEntity(manufacturerCreateDTO);
        Manufacturer savedManufacturer = manufacturerRepository.save(manufacturer);
        return entityMapper.toManufacturerDTO(savedManufacturer);
    }

    @Cacheable(value = "manufacturers", key = "#id")
    public Optional<ManufacturerDTO> getManufacturerById(UUID id) {
        return manufacturerRepository.findById(id).map(entityMapper::toManufacturerDTO);
    }

    @Cacheable(value = "allManufacturers")
    public List<ManufacturerDTO> getAllManufacturers() {
        List<Manufacturer> manufacturers = new ArrayList<>();
        manufacturerRepository.findAll().forEach(manufacturers::add);
        return entityMapper.toManufacturerDTOList(manufacturers);
    }

    @CacheEvict(value = {"allManufacturers", "manufacturers"}, key = "#id")
    public void deleteManufacturerById(UUID id) {
        manufacturerRepository.deleteById(id);
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
            return entityMapper.toManufacturerDTO(savedManufacturer);
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

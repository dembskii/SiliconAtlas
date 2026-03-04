package com.cpu.management.service;

import com.cpu.management.domain.CpuSpecification;
import com.cpu.management.dto.CpuSpecificationCreateDTO;
import com.cpu.management.dto.CpuSpecificationDTO;
import com.cpu.management.mapper.EntityMapper;
import com.cpu.management.repository.CpuSpecificationRepository;
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
public class CpuSpecificationService {

    private final CpuSpecificationRepository cpuSpecificationRepository;
    private final EntityMapper entityMapper;

    // =====================================================
    // METODY DTO - dla REST API
    // =====================================================

    @CacheEvict(value = {"allSpecifications", "specifications"}, allEntries = true)
    public CpuSpecificationDTO addSpecification(CpuSpecificationCreateDTO specificationCreateDTO) {
        CpuSpecification specification = entityMapper.toCpuSpecificationEntity(specificationCreateDTO);
        CpuSpecification savedSpecification = cpuSpecificationRepository.save(specification);
        return entityMapper.toCpuSpecificationDTO(savedSpecification);
    }

    @Cacheable(value = "specifications", key = "#id")
    public Optional<CpuSpecificationDTO> getSpecificationById(UUID id) {
        return cpuSpecificationRepository.findById(id).map(entityMapper::toCpuSpecificationDTO);
    }

    @Cacheable(value = "allSpecifications")
    public List<CpuSpecificationDTO> getAllSpecifications() {
        List<CpuSpecification> specifications = new ArrayList<>();
        cpuSpecificationRepository.findAll().forEach(specifications::add);
        return entityMapper.toCpuSpecificationDTOList(specifications);
    }

    @CacheEvict(value = {"allSpecifications", "specifications"}, key = "#id")
    public void deleteSpecificationById(UUID id) {
        cpuSpecificationRepository.deleteById(id);
    }

    // =====================================================
    // METODY WEWNĘTRZNE (encje) - dla AdminController/Thymeleaf
    // =====================================================

    public CpuSpecification addSpecificationEntity(CpuSpecification specification) {
        return cpuSpecificationRepository.save(specification);
    }

    public Optional<CpuSpecification> getSpecificationEntityById(UUID id) {
        return cpuSpecificationRepository.findById(id);
    }

    public List<CpuSpecification> getAllSpecificationEntities() {
        List<CpuSpecification> specifications = new ArrayList<>();
        cpuSpecificationRepository.findAll().forEach(specifications::add);
        return specifications;
    }
}

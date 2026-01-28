package dominik.dembski.lab05.service;

import dominik.dembski.lab05.domain.CpuSpecification;
import dominik.dembski.lab05.dto.CpuSpecificationCreateDTO;
import dominik.dembski.lab05.dto.CpuSpecificationDTO;
import dominik.dembski.lab05.mapper.EntityMapper;
import dominik.dembski.lab05.repository.CpuSpecificationRepository;
import lombok.RequiredArgsConstructor;
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

    public CpuSpecificationDTO addSpecification(CpuSpecificationCreateDTO specificationCreateDTO) {
        CpuSpecification specification = entityMapper.toCpuSpecificationEntity(specificationCreateDTO);
        CpuSpecification savedSpecification = cpuSpecificationRepository.save(specification);
        return entityMapper.toCpuSpecificationDTO(savedSpecification);
    }

    public Optional<CpuSpecificationDTO> getSpecificationById(UUID id) {
        return cpuSpecificationRepository.findById(id).map(entityMapper::toCpuSpecificationDTO);
    }

    public List<CpuSpecificationDTO> getAllSpecifications() {
        List<CpuSpecification> specifications = new ArrayList<>();
        cpuSpecificationRepository.findAll().forEach(specifications::add);
        return entityMapper.toCpuSpecificationDTOList(specifications);
    }

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

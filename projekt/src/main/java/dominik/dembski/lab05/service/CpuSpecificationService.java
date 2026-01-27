package dominik.dembski.lab05.service;

import dominik.dembski.lab05.domain.CpuSpecification;
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

    public CpuSpecification addSpecification(CpuSpecification specification) {
        return cpuSpecificationRepository.save(specification);
    }

    public Optional<CpuSpecification> getSpecificationById(UUID id) {
        return cpuSpecificationRepository.findById(id);
    }

    public List<CpuSpecification> getAllSpecifications() {
        List<CpuSpecification> result = new ArrayList<>();
        cpuSpecificationRepository.findAll().forEach(result::add);
        return result;
    }

    public void deleteSpecificationById(UUID id) {
        cpuSpecificationRepository.deleteById(id);
    }
}

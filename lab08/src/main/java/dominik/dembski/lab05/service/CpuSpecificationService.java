package dominik.dembski.lab05.service;

import dominik.dembski.lab05.domain.CpuSpecification;
import dominik.dembski.lab05.repository.CpuSpecificationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CpuSpecificationService {

    private final CpuSpecificationRepository cpuSpecificationRepository;

    public CpuSpecificationService(CpuSpecificationRepository cpuSpecificationRepository) {
        this.cpuSpecificationRepository = cpuSpecificationRepository;
    }

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

    public CpuSpecification updateSpecification(UUID id, CpuSpecification specificationDetails) {
        Optional<CpuSpecification> specification = cpuSpecificationRepository.findById(id);
        if (specification.isPresent()) {
            CpuSpecification existingSpec = specification.get();
            if (specificationDetails.getCacheL1KB() > 0) {
                existingSpec.setCacheL1KB(specificationDetails.getCacheL1KB());
            }
            if (specificationDetails.getCacheL2KB() > 0) {
                existingSpec.setCacheL2KB(specificationDetails.getCacheL2KB());
            }
            if (specificationDetails.getCacheL3MB() > 0) {
                existingSpec.setCacheL3MB(specificationDetails.getCacheL3MB());
            }
            if (specificationDetails.getTdpWatts() > 0) {
                existingSpec.setTdpWatts(specificationDetails.getTdpWatts());
            }
            if (specificationDetails.getSocketType() != null) {
                existingSpec.setSocketType(specificationDetails.getSocketType());
            }
            return cpuSpecificationRepository.save(existingSpec);
        }
        return null;
    }
}

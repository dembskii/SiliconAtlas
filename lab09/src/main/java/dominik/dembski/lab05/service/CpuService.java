package dominik.dembski.lab05.service;

import dominik.dembski.lab05.domain.Cpu;
import dominik.dembski.lab05.repository.CpuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CpuService {

    private final CpuRepository cpuRepository;

    public CpuService(CpuRepository cpuRepository) {
        this.cpuRepository = cpuRepository;
    }

    public Cpu addCpu(Cpu cpu) {
        return cpuRepository.save(cpu);
    }

    public Optional<Cpu> getCpuById(UUID id) {
        return cpuRepository.findById(id);
    }

    public List<Cpu> getAllCpus() {
        List<Cpu> result = new ArrayList<>();
        cpuRepository.findAll().forEach(result::add);
        return result;
    }

    public void deleteCpuById(UUID id) {
        cpuRepository.deleteById(id);
    }

    public Cpu updateCpu(UUID id, Cpu cpuDetails) {
        Optional<Cpu> cpu = cpuRepository.findById(id);
        if (cpu.isPresent()) {
            Cpu existingCpu = cpu.get();
            if (cpuDetails.getModel() != null) {
                existingCpu.setModel(cpuDetails.getModel());
            }
            if (cpuDetails.getCores() > 0) {
                existingCpu.setCores(cpuDetails.getCores());
            }
            if (cpuDetails.getThreads() > 0) {
                existingCpu.setThreads(cpuDetails.getThreads());
            }
            if (cpuDetails.getFrequencyGhz() > 0) {
                existingCpu.setFrequencyGhz(cpuDetails.getFrequencyGhz());
            }
            return cpuRepository.save(existingCpu);
        }
        return null;
    }
}

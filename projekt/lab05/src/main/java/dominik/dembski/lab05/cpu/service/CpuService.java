package dominik.dembski.lab05.cpu.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dominik.dembski.lab05.cpu.domain.Cpu;
import dominik.dembski.lab05.cpu.repository.CpuRepository;

@Service
public class CpuService {

    private final CpuRepository cpuRepository;

    public CpuService(CpuRepository cpuRepository) {
        this.cpuRepository = cpuRepository;
    }

    public List<Cpu> getAllCpus() {
        return cpuRepository.findAll();
    }

    public Cpu getCpuById(UUID id) {
        return cpuRepository.findById(id).orElse(null);
    }

    public Cpu saveCpu(Cpu cpu) {
        return cpuRepository.save(cpu);
    }

    public void deleteCpu(UUID id) {
        cpuRepository.deleteById(id);
    }
}

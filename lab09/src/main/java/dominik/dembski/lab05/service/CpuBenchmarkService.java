package dominik.dembski.lab05.service;

import dominik.dembski.lab05.domain.CpuBenchmark;
import dominik.dembski.lab05.repository.CpuBenchmarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CpuBenchmarkService {

    private final CpuBenchmarkRepository cpuBenchmarkRepository;

    public CpuBenchmarkService(CpuBenchmarkRepository cpuBenchmarkRepository) {
        this.cpuBenchmarkRepository = cpuBenchmarkRepository;
    }

    public CpuBenchmark addBenchmark(CpuBenchmark benchmark) {
        return cpuBenchmarkRepository.save(benchmark);
    }

    public Optional<CpuBenchmark> getBenchmarkById(UUID id) {
        return cpuBenchmarkRepository.findById(id);
    }

    public List<CpuBenchmark> getAllBenchmarks() {
        List<CpuBenchmark> result = new ArrayList<>();
        cpuBenchmarkRepository.findAll().forEach(result::add);
        return result;
    }

    public void deleteBenchmarkById(UUID id) {
        cpuBenchmarkRepository.deleteById(id);
    }

    public CpuBenchmark updateBenchmark(UUID id, CpuBenchmark benchmarkDetails) {
        Optional<CpuBenchmark> benchmark = cpuBenchmarkRepository.findById(id);
        if (benchmark.isPresent()) {
            CpuBenchmark existingBenchmark = benchmark.get();
            if (benchmarkDetails.getSingleCoreScore() > 0) {
                existingBenchmark.setSingleCoreScore(benchmarkDetails.getSingleCoreScore());
            }
            if (benchmarkDetails.getMultiCoreScore() > 0) {
                existingBenchmark.setMultiCoreScore(benchmarkDetails.getMultiCoreScore());
            }
            if (benchmarkDetails.getPassmarkScore() > 0) {
                existingBenchmark.setPassmarkScore(benchmarkDetails.getPassmarkScore());
            }
            if (benchmarkDetails.getCinebenchR23() > 0) {
                existingBenchmark.setCinebenchR23(benchmarkDetails.getCinebenchR23());
            }
            if (benchmarkDetails.getTestDate() != null) {
                existingBenchmark.setTestDate(benchmarkDetails.getTestDate());
            }
            return cpuBenchmarkRepository.save(existingBenchmark);
        }
        return null;
    }
}

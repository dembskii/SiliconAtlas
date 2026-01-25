package dominik.dembski.lab05.service;

import dominik.dembski.lab05.domain.Cpu;
import dominik.dembski.lab05.domain.CpuBenchmark;
import dominik.dembski.lab05.dto.BenchmarkStatsDTO;
import dominik.dembski.lab05.repository.CpuBenchmarkRepository;
import dominik.dembski.lab05.repository.CpuRepository;
import dominik.dembski.lab05.repository.ManufacturerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CpuBenchmarkService {

    private final CpuBenchmarkRepository cpuBenchmarkRepository;
    private final CpuRepository cpuRepository;
    private final ManufacturerRepository manufacturerRepository;

    public CpuBenchmarkService(CpuBenchmarkRepository cpuBenchmarkRepository,
                                CpuRepository cpuRepository,
                                ManufacturerRepository manufacturerRepository) {
        this.cpuBenchmarkRepository = cpuBenchmarkRepository;
        this.cpuRepository = cpuRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    // =====================================================
    // PODSTAWOWE OPERACJE CRUD
    // =====================================================

    public CpuBenchmark addBenchmark(CpuBenchmark benchmark) {
        // Jeśli benchmark ma CPU, pobierz go z bazy aby mieć attached entity
        if (benchmark.getCpu() != null && benchmark.getCpu().getId() != null) {
            Cpu attachedCpu = cpuRepository.findById(benchmark.getCpu().getId()).orElse(null);
            if (attachedCpu != null) {
                benchmark.setCpu(attachedCpu);
            }
        }
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

    // =====================================================
    // ROZBUDOWANA LOGIKA BIZNESOWA (2+ repozytoriów)
    // =====================================================

    /**
     * Dodaje benchmark do konkretnego CPU.
     * Wykorzystuje: CpuBenchmarkRepository, CpuRepository
     */
    public CpuBenchmark addBenchmarkToCpu(UUID cpuId, CpuBenchmark benchmark) {
        Optional<Cpu> cpuOpt = cpuRepository.findById(cpuId);
        if (cpuOpt.isEmpty()) {
            throw new IllegalArgumentException("CPU not found with id: " + cpuId);
        }
        
        benchmark.setCpu(cpuOpt.get());
        return cpuBenchmarkRepository.save(benchmark);
    }

    /**
     * Pobiera statystyki benchmarków dla wszystkich CPU danego producenta.
     * Wykorzystuje: CpuBenchmarkRepository, ManufacturerRepository
     */
    public List<BenchmarkStatsDTO> getBenchmarkStatsByManufacturer(String manufacturerName) {
        if (manufacturerRepository.findByName(manufacturerName).isEmpty()) {
            return new ArrayList<>();
        }

        List<CpuBenchmark> benchmarks = cpuBenchmarkRepository.findBenchmarksByManufacturer(manufacturerName);
        
        Map<String, List<CpuBenchmark>> benchmarksByCpu = benchmarks.stream()
            .filter(b -> b.getCpu() != null)
            .collect(Collectors.groupingBy(b -> b.getCpu().getModel()));

        return benchmarksByCpu.entrySet().stream()
            .map(entry -> {
                List<CpuBenchmark> cpuBenchmarks = entry.getValue();
                BenchmarkStatsDTO stats = new BenchmarkStatsDTO();
                stats.setCpuModel(entry.getKey());
                stats.setManufacturerName(manufacturerName);
                stats.setBenchmarkCount((long) cpuBenchmarks.size());
                stats.setAvgSingleCore(cpuBenchmarks.stream()
                    .mapToInt(CpuBenchmark::getSingleCoreScore).average().orElse(0));
                stats.setAvgMultiCore(cpuBenchmarks.stream()
                    .mapToInt(CpuBenchmark::getMultiCoreScore).average().orElse(0));
                stats.setAvgPassmark(cpuBenchmarks.stream()
                    .mapToInt(CpuBenchmark::getPassmarkScore).average().orElse(0));
                stats.setAvgCinebench(cpuBenchmarks.stream()
                    .mapToDouble(CpuBenchmark::getCinebenchR23).average().orElse(0));
                return stats;
            })
            .sorted(Comparator.comparing(BenchmarkStatsDTO::getAvgPassmark).reversed())
            .collect(Collectors.toList());
    }

    /**
     * Tworzy ranking CPU na podstawie benchmarków.
     * Wykorzystuje: CpuBenchmarkRepository (JPQL GROUP BY)
     */
    public List<Map<String, Object>> createCpuRanking(String sortBy, int limit) {
        List<BenchmarkStatsDTO> allStats = cpuBenchmarkRepository.getBenchmarkStatsByCpu();
        
        Comparator<BenchmarkStatsDTO> comparator = switch (sortBy.toLowerCase()) {
            case "singlecore" -> Comparator.comparing(BenchmarkStatsDTO::getAvgSingleCore,
                    Comparator.nullsLast(Comparator.reverseOrder()));
            case "multicore" -> Comparator.comparing(BenchmarkStatsDTO::getAvgMultiCore,
                    Comparator.nullsLast(Comparator.reverseOrder()));
            default -> Comparator.comparing(BenchmarkStatsDTO::getAvgPassmark,
                    Comparator.nullsLast(Comparator.reverseOrder()));
        };

        List<BenchmarkStatsDTO> sorted = allStats.stream()
            .sorted(comparator)
            .limit(limit)
            .collect(Collectors.toList());

        List<Map<String, Object>> ranking = new ArrayList<>();
        int rank = 1;
        for (BenchmarkStatsDTO stats : sorted) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("rank", rank++);
            entry.put("cpuModel", stats.getCpuModel());
            entry.put("manufacturer", stats.getManufacturerName());
            entry.put("avgSingleCore", stats.getAvgSingleCore());
            entry.put("avgMultiCore", stats.getAvgMultiCore());
            entry.put("avgPassmark", stats.getAvgPassmark());
            entry.put("benchmarkCount", stats.getBenchmarkCount());
            ranking.add(entry);
        }

        return ranking;
    }

    /**
     * Oblicza współczynnik skalowania wielowątkowego dla CPU.
     * Wykorzystuje: CpuBenchmarkRepository (Native Query)
     */
    public List<Map<String, Object>> calculateMultithreadingScaling() {
        List<Object[]> data = cpuBenchmarkRepository.getCpuScalingFactorNative();
        
        return data.stream().map(row -> {
            Map<String, Object> result = new HashMap<>();
            result.put("cpuModel", row[0]);
            result.put("avgSingleCore", row[1]);
            result.put("avgMultiCore", row[2]);
            result.put("scalingFactor", row[3]);
            return result;
        }).collect(Collectors.toList());
    }

    /**
     * Pobiera wszystkie statystyki benchmarków pogrupowane po CPU.
     */
    public List<BenchmarkStatsDTO> getAllBenchmarkStatsByCpu() {
        return cpuBenchmarkRepository.getBenchmarkStatsByCpu();
    }
}

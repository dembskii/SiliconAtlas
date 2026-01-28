package dominik.dembski.lab05.controller;

import dominik.dembski.lab05.domain.CpuBenchmark;
import dominik.dembski.lab05.dto.BenchmarkStatsDTO;
import dominik.dembski.lab05.dto.CpuBenchmarkCreateDTO;
import dominik.dembski.lab05.dto.CpuBenchmarkDTO;
import dominik.dembski.lab05.mapper.EntityMapper;
import dominik.dembski.lab05.service.CpuBenchmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/benchmarks")
@RequiredArgsConstructor
public class CpuBenchmarkController {

    private final CpuBenchmarkService cpuBenchmarkService;
    private final EntityMapper entityMapper;

    // =====================================================
    // PODSTAWOWE OPERACJE CRUD
    // =====================================================

    @PostMapping
    public ResponseEntity<CpuBenchmarkDTO> addBenchmark(@RequestBody CpuBenchmarkCreateDTO benchmarkCreateDTO) {
        CpuBenchmark benchmark = entityMapper.toCpuBenchmarkEntity(benchmarkCreateDTO);
        CpuBenchmark savedBenchmark = cpuBenchmarkService.addBenchmark(benchmark);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityMapper.toCpuBenchmarkDTO(savedBenchmark));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CpuBenchmarkDTO> getBenchmarkById(@PathVariable UUID id) {
        return cpuBenchmarkService.getBenchmarkById(id)
                .map(benchmark -> ResponseEntity.ok(entityMapper.toCpuBenchmarkDTO(benchmark)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CpuBenchmarkDTO>> getAllBenchmarks() {
        return ResponseEntity.ok(entityMapper.toCpuBenchmarkDTOList(cpuBenchmarkService.getAllBenchmarks()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CpuBenchmarkDTO> updateBenchmark(@PathVariable UUID id, @RequestBody CpuBenchmarkCreateDTO benchmarkCreateDTO) {
        CpuBenchmark benchmarkDetails = entityMapper.toCpuBenchmarkEntity(benchmarkCreateDTO);
        CpuBenchmark updatedBenchmark = cpuBenchmarkService.updateBenchmark(id, benchmarkDetails);
        if (updatedBenchmark != null) {
            return ResponseEntity.ok(entityMapper.toCpuBenchmarkDTO(updatedBenchmark));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBenchmarkById(@PathVariable UUID id) {
        cpuBenchmarkService.deleteBenchmarkById(id);
        return ResponseEntity.noContent().build();
    }

    // =====================================================
    // ENDPOINTY DLA ROZBUDOWANEJ LOGIKI BIZNESOWEJ
    // =====================================================

    /**
     * Dodaje benchmark do konkretnego CPU.
     * POST /api/benchmarks/cpu/{cpuId}
     */
    @PostMapping("/cpu/{cpuId}")
    public ResponseEntity<?> addBenchmarkToCpu(
            @PathVariable UUID cpuId,
            @RequestBody CpuBenchmarkCreateDTO benchmarkCreateDTO) {
        try {
            CpuBenchmark benchmark = entityMapper.toCpuBenchmarkEntity(benchmarkCreateDTO);
            CpuBenchmark saved = cpuBenchmarkService.addBenchmarkToCpu(cpuId, benchmark);
            return ResponseEntity.status(HttpStatus.CREATED).body(entityMapper.toCpuBenchmarkDTO(saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Pobiera statystyki benchmarków dla producenta.
     * GET /api/benchmarks/stats/manufacturer/{name}
     */
    @GetMapping("/stats/manufacturer/{name}")
    public ResponseEntity<List<BenchmarkStatsDTO>> getBenchmarkStatsByManufacturer(
            @PathVariable String name) {
        List<BenchmarkStatsDTO> stats = cpuBenchmarkService.getBenchmarkStatsByManufacturer(name);
        return ResponseEntity.ok(stats);
    }

    /**
     * Tworzy ranking CPU na podstawie benchmarków.
     * GET /api/benchmarks/ranking?sortBy={field}&limit={n}
     */
    @GetMapping("/ranking")
    public ResponseEntity<List<Map<String, Object>>> getCpuRanking(
            @RequestParam(defaultValue = "passmark") String sortBy,
            @RequestParam(defaultValue = "20") int limit) {
        List<Map<String, Object>> ranking = cpuBenchmarkService.createCpuRanking(sortBy, limit);
        return ResponseEntity.ok(ranking);
    }

    /**
     * Oblicza współczynnik skalowania wielowątkowego.
     * GET /api/benchmarks/scaling-factor
     */
    @GetMapping("/scaling-factor")
    public ResponseEntity<List<Map<String, Object>>> getMultithreadingScaling() {
        List<Map<String, Object>> scaling = cpuBenchmarkService.calculateMultithreadingScaling();
        return ResponseEntity.ok(scaling);
    }

    /**
     * Pobiera wszystkie statystyki benchmarków pogrupowane po CPU.
     * GET /api/benchmarks/stats/all
     */
    @GetMapping("/stats/all")
    public ResponseEntity<List<BenchmarkStatsDTO>> getAllBenchmarkStats() {
        List<BenchmarkStatsDTO> stats = cpuBenchmarkService.getAllBenchmarkStatsByCpu();
        return ResponseEntity.ok(stats);
    }
}

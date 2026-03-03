package com.cpu.management.controller;

import com.cpu.management.dto.BenchmarkStatsDTO;
import com.cpu.management.dto.CpuBenchmarkCreateDTO;
import com.cpu.management.dto.CpuBenchmarkDTO;
import com.cpu.management.service.CpuBenchmarkService;
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

    // =====================================================
    // PODSTAWOWE OPERACJE CRUD
    // =====================================================

    @PostMapping
    public ResponseEntity<CpuBenchmarkDTO> addBenchmark(@RequestBody CpuBenchmarkCreateDTO benchmarkCreateDTO) {
        CpuBenchmarkDTO savedBenchmark = cpuBenchmarkService.addBenchmark(benchmarkCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBenchmark);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CpuBenchmarkDTO> getBenchmarkById(@PathVariable UUID id) {
        return cpuBenchmarkService.getBenchmarkById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CpuBenchmarkDTO>> getAllBenchmarks() {
        return ResponseEntity.ok(cpuBenchmarkService.getAllBenchmarks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CpuBenchmarkDTO> updateBenchmark(@PathVariable UUID id, @RequestBody CpuBenchmarkCreateDTO benchmarkCreateDTO) {
        CpuBenchmarkDTO updatedBenchmark = cpuBenchmarkService.updateBenchmark(id, benchmarkCreateDTO);
        if (updatedBenchmark != null) {
            return ResponseEntity.ok(updatedBenchmark);
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
            CpuBenchmarkDTO savedBenchmark = cpuBenchmarkService.addBenchmarkToCpu(cpuId, benchmarkCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBenchmark);
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

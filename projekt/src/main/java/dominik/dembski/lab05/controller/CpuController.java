package dominik.dembski.lab05.controller;

import dominik.dembski.lab05.domain.Cpu;
import dominik.dembski.lab05.dto.CpuComparisonDTO;
import dominik.dembski.lab05.dto.CpuPerformanceDTO;
import dominik.dembski.lab05.dto.ManufacturerStatsDTO;
import dominik.dembski.lab05.service.CpuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/cpus")
public class CpuController {

    private final CpuService cpuService;

    public CpuController(CpuService cpuService) {
        this.cpuService = cpuService;
    }

    // =====================================================
    // PODSTAWOWE OPERACJE CRUD
    // =====================================================

    @PostMapping
    public ResponseEntity<Cpu> addCpu(@RequestBody Cpu cpu) {
        Cpu savedCpu = cpuService.addCpu(cpu);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCpu);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cpu> getCpuById(@PathVariable UUID id) {
        Optional<Cpu> cpu = cpuService.getCpuById(id);
        return cpu.map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Cpu>> getAllCpus() {
        return ResponseEntity.ok(cpuService.getAllCpus());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cpu> updateCpu(@PathVariable UUID id, @RequestBody Cpu cpuDetails) {
        Cpu updatedCpu = cpuService.updateCpu(id, cpuDetails);
        if (updatedCpu != null) {
            return ResponseEntity.ok(updatedCpu);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCpuById(@PathVariable UUID id) {
        cpuService.deleteCpuById(id);
        return ResponseEntity.noContent().build();
    }

    // =====================================================
    // ENDPOINTY DLA ROZBUDOWANEJ LOGIKI BIZNESOWEJ
    // =====================================================

    /**
     * Tworzy CPU z przypisaniem producenta i technologii.
     * POST /api/cpus/with-relations
     */
    @PostMapping("/with-relations")
    public ResponseEntity<?> createCpuWithRelations(
            @RequestBody Cpu cpu,
            @RequestParam UUID manufacturerId,
            @RequestParam(required = false) List<UUID> technologyIds) {
        try {
            Cpu createdCpu = cpuService.createCpuWithManufacturerAndTechnologies(cpu, manufacturerId, technologyIds);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCpu);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Porównuje dwa procesory.
     * GET /api/cpus/compare?cpu1={id}&cpu2={id}
     */
    @GetMapping("/compare")
    public ResponseEntity<?> compareCpus(
            @RequestParam UUID cpu1,
            @RequestParam UUID cpu2) {
        try {
            CpuComparisonDTO comparison = cpuService.compareCpus(cpu1, cpu2);
            return ResponseEntity.ok(comparison);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Pobiera najwydajniejsze CPU producenta.
     * GET /api/cpus/top-by-manufacturer?manufacturer={name}&limit={n}
     */
    @GetMapping("/top-by-manufacturer")
    public ResponseEntity<List<CpuPerformanceDTO>> getTopCpusByManufacturer(
            @RequestParam String manufacturer,
            @RequestParam(defaultValue = "10") int limit) {
        List<CpuPerformanceDTO> topCpus = cpuService.getTopPerformingCpusByManufacturer(manufacturer, limit);
        return ResponseEntity.ok(topCpus);
    }

    /**
     * Pobiera statystyki producentów.
     * GET /api/cpus/manufacturer-stats
     */
    @GetMapping("/manufacturer-stats")
    public ResponseEntity<List<ManufacturerStatsDTO>> getManufacturerStats() {
        List<ManufacturerStatsDTO> stats = cpuService.getManufacturerPerformanceStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * Rekomenduje CPU na podstawie wymagań.
     * GET /api/cpus/recommend?minCores={n}&minFrequency={ghz}&minBenchmark={score}
     */
    @GetMapping("/recommend")
    public ResponseEntity<List<CpuPerformanceDTO>> recommendCpus(
            @RequestParam(defaultValue = "4") int minCores,
            @RequestParam(defaultValue = "2.0") double minFrequency,
            @RequestParam(required = false) Integer minBenchmark) {
        List<CpuPerformanceDTO> recommendations = cpuService.recommendCpus(minCores, minFrequency, minBenchmark);
        return ResponseEntity.ok(recommendations);
    }
}

package dominik.dembski.lab05.controller;

import dominik.dembski.lab05.domain.CpuBenchmark;
import dominik.dembski.lab05.service.CpuBenchmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/benchmarks")
public class CpuBenchmarkController {

    private final CpuBenchmarkService cpuBenchmarkService;

    public CpuBenchmarkController(CpuBenchmarkService cpuBenchmarkService) {
        this.cpuBenchmarkService = cpuBenchmarkService;
    }

    @PostMapping
    public ResponseEntity<CpuBenchmark> addBenchmark(@RequestBody CpuBenchmark benchmark) {
        CpuBenchmark savedBenchmark = cpuBenchmarkService.addBenchmark(benchmark);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBenchmark);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CpuBenchmark> getBenchmarkById(@PathVariable UUID id) {
        Optional<CpuBenchmark> benchmark = cpuBenchmarkService.getBenchmarkById(id);
        return benchmark.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Iterable<CpuBenchmark>> getAllBenchmarks() {
        return ResponseEntity.ok(cpuBenchmarkService.getAllBenchmarks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CpuBenchmark> updateBenchmark(@PathVariable UUID id, @RequestBody CpuBenchmark benchmarkDetails) {
        CpuBenchmark updatedBenchmark = cpuBenchmarkService.updateBenchmark(id, benchmarkDetails);
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
}

package dominik.dembski.lab05.controller;

import dominik.dembski.lab05.domain.Cpu;
import dominik.dembski.lab05.service.CpuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/cpus")
public class CpuController {

    private final CpuService cpuService;

    public CpuController(CpuService cpuService) {
        this.cpuService = cpuService;
    }

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
    public ResponseEntity<Iterable<Cpu>> getAllCpus() {
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
}

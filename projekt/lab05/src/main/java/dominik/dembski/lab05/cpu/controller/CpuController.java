package dominik.dembski.lab05.cpu.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dominik.dembski.lab05.cpu.domain.Cpu;
import dominik.dembski.lab05.cpu.service.CpuService;

@RestController
@RequestMapping("/api/cpus")
public class CpuController {

    private final CpuService cpuService;

    public CpuController(CpuService cpuService) {
        this.cpuService = cpuService;
    }

    @GetMapping
    public ResponseEntity<List<Cpu>> getAllCpus() {
        return ResponseEntity.ok(cpuService.getAllCpus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cpu> getCpuById(@PathVariable UUID id) {
        Cpu cpu = cpuService.getCpuById(id);
        if (cpu != null) {
            return ResponseEntity.ok(cpu);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Cpu> createCpu(@RequestBody Cpu cpu) {
        Cpu savedCpu = cpuService.saveCpu(cpu);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCpu);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCpu(@PathVariable UUID id) {
        cpuService.deleteCpu(id);
        return ResponseEntity.noContent().build();
    }
}

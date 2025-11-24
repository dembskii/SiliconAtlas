package ug.lab04.procesor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ug.lab04.procesor.cpu.Cpu;
import ug.lab04.procesor.exception.CpuNotFoundException;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/cpu")
public class CpuController {

    private final Map<String, Cpu> cpuDatabase = new HashMap<>();

    // GET - Wyświetlenie wszystkich encji w formacie JSON
    @GetMapping
    public ResponseEntity<Collection<Cpu>> getAllCpus() {
        return ResponseEntity.ok(cpuDatabase.values());
    }

    // GET - Wyświetlenie konkretnej encji po ID w formacie JSON
    @GetMapping("/{id}")
    public ResponseEntity<Cpu> getCpuById(@PathVariable String id) {
        Cpu cpu = cpuDatabase.get(id);
        
        if (cpu == null) {
            throw new CpuNotFoundException(id);
        }
        
        return ResponseEntity.ok(cpu);
    }

    // POST - Dodanie nowej encji do bazy
    @PostMapping
    public ResponseEntity<Cpu> addCpu(@RequestBody Cpu cpu) {
        if (cpu == null) {
            throw new IllegalArgumentException("CPU object cannot be null");
        }
        
        String id = cpu.getId().toString();
        cpuDatabase.put(id, cpu);
        
        // Ustawienie nagłówka Location zgodnie z best practices
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        
        return ResponseEntity.created(location).body(cpu);
    }

    // PUT - Edycja encji z bazy
    @PutMapping("/{id}")
    public ResponseEntity<Cpu> updateCpu(@PathVariable String id, @RequestBody Cpu updatedCpu) {
        if (!cpuDatabase.containsKey(id)) {
            throw new CpuNotFoundException(id);
        }
        
        Cpu existingCpu = cpuDatabase.get(id);

        if (!updatedCpu.getProducer().isEmpty()) {
            existingCpu.setProducer(updatedCpu.getProducer());
        }
        if (updatedCpu.getCores() != 0) {
            existingCpu.setCores(updatedCpu.getCores());
        }
        if (updatedCpu.getThreads() != 0) {
            existingCpu.setThreads(updatedCpu.getThreads());
        }
        if (updatedCpu.getFrequencyGhz() != 0) {
            existingCpu.setFrequencyGhz(updatedCpu.getFrequencyGhz());
        }
        return ResponseEntity.ok(existingCpu);
    }

    // DELETE - Usunięcie encji z bazy
    @DeleteMapping("/{id}")
    public ResponseEntity<Cpu> deleteCpu(@PathVariable String id) {
        if (!cpuDatabase.containsKey(id)) {
            throw new CpuNotFoundException(id);
        }

        Cpu removed = cpuDatabase.remove(id);

        return ResponseEntity.ok(removed);
    }

    // Metoda pomocnicza do inicjalizacji bazy danych (używana przez CommandLineRunner)
    public void initializeDatabase(List<Cpu> cpus) {
        for (Cpu cpu : cpus) {
            cpuDatabase.put(cpu.getId().toString(), cpu);
        }
    }
}

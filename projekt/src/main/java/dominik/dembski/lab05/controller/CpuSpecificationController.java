package dominik.dembski.lab05.controller;

import dominik.dembski.lab05.domain.CpuSpecification;
import dominik.dembski.lab05.service.CpuSpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/specifications")
@RequiredArgsConstructor
public class CpuSpecificationController {

    private final CpuSpecificationService cpuSpecificationService;

    @PostMapping
    public ResponseEntity<CpuSpecification> addSpecification(@RequestBody CpuSpecification specification) {
        CpuSpecification savedSpecification = cpuSpecificationService.addSpecification(specification);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSpecification);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CpuSpecification> getSpecificationById(@PathVariable UUID id) {
        Optional<CpuSpecification> specification = cpuSpecificationService.getSpecificationById(id);
        return specification.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CpuSpecification>> getAllSpecifications() {
        return ResponseEntity.ok(cpuSpecificationService.getAllSpecifications());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecificationById(@PathVariable UUID id) {
        cpuSpecificationService.deleteSpecificationById(id);
        return ResponseEntity.noContent().build();
    }
}

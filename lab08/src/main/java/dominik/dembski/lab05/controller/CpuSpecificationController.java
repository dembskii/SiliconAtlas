package dominik.dembski.lab05.controller;

import dominik.dembski.lab05.domain.CpuSpecification;
import dominik.dembski.lab05.service.CpuSpecificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/specifications")
public class CpuSpecificationController {

    private final CpuSpecificationService cpuSpecificationService;

    public CpuSpecificationController(CpuSpecificationService cpuSpecificationService) {
        this.cpuSpecificationService = cpuSpecificationService;
    }

    @PostMapping
    public ResponseEntity<CpuSpecification> addSpecification(@RequestBody CpuSpecification specification) {
        CpuSpecification savedSpec = cpuSpecificationService.addSpecification(specification);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSpec);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CpuSpecification> getSpecificationById(@PathVariable UUID id) {
        Optional<CpuSpecification> specification = cpuSpecificationService.getSpecificationById(id);
        return specification.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Iterable<CpuSpecification>> getAllSpecifications() {
        return ResponseEntity.ok(cpuSpecificationService.getAllSpecifications());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CpuSpecification> updateSpecification(@PathVariable UUID id, @RequestBody CpuSpecification specificationDetails) {
        CpuSpecification updatedSpec = cpuSpecificationService.updateSpecification(id, specificationDetails);
        if (updatedSpec != null) {
            return ResponseEntity.ok(updatedSpec);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecificationById(@PathVariable UUID id) {
        cpuSpecificationService.deleteSpecificationById(id);
        return ResponseEntity.noContent().build();
    }
}

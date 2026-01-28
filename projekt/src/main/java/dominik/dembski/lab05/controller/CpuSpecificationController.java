package dominik.dembski.lab05.controller;

import dominik.dembski.lab05.domain.CpuSpecification;
import dominik.dembski.lab05.dto.CpuSpecificationCreateDTO;
import dominik.dembski.lab05.dto.CpuSpecificationDTO;
import dominik.dembski.lab05.mapper.EntityMapper;
import dominik.dembski.lab05.service.CpuSpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/specifications")
@RequiredArgsConstructor
public class CpuSpecificationController {

    private final CpuSpecificationService cpuSpecificationService;
    private final EntityMapper entityMapper;

    @PostMapping
    public ResponseEntity<CpuSpecificationDTO> addSpecification(@RequestBody CpuSpecificationCreateDTO specificationCreateDTO) {
        CpuSpecification specification = entityMapper.toCpuSpecificationEntity(specificationCreateDTO);
        CpuSpecification savedSpecification = cpuSpecificationService.addSpecification(specification);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityMapper.toCpuSpecificationDTO(savedSpecification));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CpuSpecificationDTO> getSpecificationById(@PathVariable UUID id) {
        return cpuSpecificationService.getSpecificationById(id)
                .map(specification -> ResponseEntity.ok(entityMapper.toCpuSpecificationDTO(specification)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CpuSpecificationDTO>> getAllSpecifications() {
        return ResponseEntity.ok(entityMapper.toCpuSpecificationDTOList(cpuSpecificationService.getAllSpecifications()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecificationById(@PathVariable UUID id) {
        cpuSpecificationService.deleteSpecificationById(id);
        return ResponseEntity.noContent().build();
    }
}

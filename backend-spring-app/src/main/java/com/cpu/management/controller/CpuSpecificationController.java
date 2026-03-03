package com.cpu.management.controller;

import com.cpu.management.dto.CpuSpecificationCreateDTO;
import com.cpu.management.dto.CpuSpecificationDTO;
import com.cpu.management.service.CpuSpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/specifications")
@RequiredArgsConstructor
public class CpuSpecificationController {

    private final CpuSpecificationService cpuSpecificationService;

    @PostMapping
    public ResponseEntity<CpuSpecificationDTO> addSpecification(@RequestBody CpuSpecificationCreateDTO specificationCreateDTO) {
        CpuSpecificationDTO savedSpecification = cpuSpecificationService.addSpecification(specificationCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSpecification);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CpuSpecificationDTO> getSpecificationById(@PathVariable UUID id) {
        return cpuSpecificationService.getSpecificationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CpuSpecificationDTO>> getAllSpecifications() {
        return ResponseEntity.ok(cpuSpecificationService.getAllSpecifications());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecificationById(@PathVariable UUID id) {
        cpuSpecificationService.deleteSpecificationById(id);
        return ResponseEntity.noContent().build();
    }
}

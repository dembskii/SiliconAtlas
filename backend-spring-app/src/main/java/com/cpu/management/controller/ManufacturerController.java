package com.cpu.management.controller;

import com.cpu.management.dto.ManufacturerCreateDTO;
import com.cpu.management.dto.ManufacturerDTO;
import com.cpu.management.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @PostMapping
    public ResponseEntity<ManufacturerDTO> addManufacturer(@RequestBody ManufacturerCreateDTO manufacturerCreateDTO) {
        ManufacturerDTO savedManufacturer = manufacturerService.addManufacturer(manufacturerCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedManufacturer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerDTO> getManufacturerById(@PathVariable UUID id) {
        return manufacturerService.getManufacturerById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ManufacturerDTO>> getAllManufacturers() {
        return ResponseEntity.ok(manufacturerService.getAllManufacturers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManufacturerDTO> updateManufacturer(@PathVariable UUID id, @RequestBody ManufacturerCreateDTO manufacturerCreateDTO) {
        ManufacturerDTO updatedManufacturer = manufacturerService.updateManufacturer(id, manufacturerCreateDTO);
        if (updatedManufacturer != null) {
            return ResponseEntity.ok(updatedManufacturer);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturerById(@PathVariable UUID id) {
        manufacturerService.deleteManufacturerById(id);
        return ResponseEntity.noContent().build();
    }
}

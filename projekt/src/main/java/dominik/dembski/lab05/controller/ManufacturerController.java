package dominik.dembski.lab05.controller;

import dominik.dembski.lab05.domain.Manufacturer;
import dominik.dembski.lab05.dto.ManufacturerCreateDTO;
import dominik.dembski.lab05.dto.ManufacturerDTO;
import dominik.dembski.lab05.mapper.EntityMapper;
import dominik.dembski.lab05.service.ManufacturerService;
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
    private final EntityMapper entityMapper;

    @PostMapping
    public ResponseEntity<ManufacturerDTO> addManufacturer(@RequestBody ManufacturerCreateDTO manufacturerCreateDTO) {
        Manufacturer manufacturer = entityMapper.toManufacturerEntity(manufacturerCreateDTO);
        Manufacturer savedManufacturer = manufacturerService.addManufacturer(manufacturer);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityMapper.toManufacturerDTO(savedManufacturer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerDTO> getManufacturerById(@PathVariable UUID id) {
        return manufacturerService.getManufacturerById(id)
                .map(manufacturer -> ResponseEntity.ok(entityMapper.toManufacturerDTO(manufacturer)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ManufacturerDTO>> getAllManufacturers() {
        return ResponseEntity.ok(entityMapper.toManufacturerDTOList(manufacturerService.getAllManufacturers()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManufacturerDTO> updateManufacturer(@PathVariable UUID id, @RequestBody ManufacturerCreateDTO manufacturerCreateDTO) {
        Manufacturer manufacturerDetails = entityMapper.toManufacturerEntity(manufacturerCreateDTO);
        Manufacturer updatedManufacturer = manufacturerService.updateManufacturer(id, manufacturerDetails);
        if (updatedManufacturer != null) {
            return ResponseEntity.ok(entityMapper.toManufacturerDTO(updatedManufacturer));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturerById(@PathVariable UUID id) {
        manufacturerService.deleteManufacturerById(id);
        return ResponseEntity.noContent().build();
    }
}

package dominik.dembski.lab05.controller;

import dominik.dembski.lab05.domain.Manufacturer;
import dominik.dembski.lab05.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @PostMapping
    public ResponseEntity<Manufacturer> addManufacturer(@RequestBody Manufacturer manufacturer) {
        Manufacturer savedManufacturer = manufacturerService.addManufacturer(manufacturer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedManufacturer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manufacturer> getManufacturerById(@PathVariable UUID id) {
        Optional<Manufacturer> manufacturer = manufacturerService.getManufacturerById(id);
        return manufacturer.map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Manufacturer>> getAllManufacturers() {
        return ResponseEntity.ok(manufacturerService.getAllManufacturers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manufacturer> updateManufacturer(@PathVariable UUID id, @RequestBody Manufacturer manufacturerDetails) {
        Manufacturer updatedManufacturer = manufacturerService.updateManufacturer(id, manufacturerDetails);
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

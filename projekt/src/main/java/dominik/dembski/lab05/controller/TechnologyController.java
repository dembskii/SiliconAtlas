package dominik.dembski.lab05.controller;

import dominik.dembski.lab05.domain.Technology;
import dominik.dembski.lab05.service.TechnologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/technologies")
@RequiredArgsConstructor
public class TechnologyController {

    private final TechnologyService technologyService;

    @PostMapping
    public ResponseEntity<Technology> addTechnology(@RequestBody Technology technology) {
        Technology savedTechnology = technologyService.addTechnology(technology);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTechnology);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Technology> getTechnologyById(@PathVariable UUID id) {
        Optional<Technology> technology = technologyService.getTechnologyById(id);
        return technology.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Technology>> getAllTechnologies() {
        return ResponseEntity.ok(technologyService.getAllTechnologies());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Technology> updateTechnology(@PathVariable UUID id, @RequestBody Technology technologyDetails) {
        Technology updatedTechnology = technologyService.updateTechnology(id, technologyDetails);
        if (updatedTechnology != null) {
            return ResponseEntity.ok(updatedTechnology);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnologyById(@PathVariable UUID id) {
        technologyService.deleteTechnologyById(id);
        return ResponseEntity.noContent().build();
    }
}

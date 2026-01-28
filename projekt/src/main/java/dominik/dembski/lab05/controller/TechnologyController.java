package dominik.dembski.lab05.controller;

import dominik.dembski.lab05.dto.TechnologyCreateDTO;
import dominik.dembski.lab05.dto.TechnologyDTO;
import dominik.dembski.lab05.service.TechnologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/technologies")
@RequiredArgsConstructor
public class TechnologyController {

    private final TechnologyService technologyService;

    @PostMapping
    public ResponseEntity<TechnologyDTO> addTechnology(@RequestBody TechnologyCreateDTO technologyCreateDTO) {
        TechnologyDTO savedTechnology = technologyService.addTechnology(technologyCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTechnology);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnologyDTO> getTechnologyById(@PathVariable UUID id) {
        return technologyService.getTechnologyById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TechnologyDTO>> getAllTechnologies() {
        return ResponseEntity.ok(technologyService.getAllTechnologies());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnologyDTO> updateTechnology(@PathVariable UUID id, @RequestBody TechnologyCreateDTO technologyCreateDTO) {
        TechnologyDTO updatedTechnology = technologyService.updateTechnology(id, technologyCreateDTO);
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

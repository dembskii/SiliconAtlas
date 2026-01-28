package dominik.dembski.lab05.controller;

import dominik.dembski.lab05.domain.Technology;
import dominik.dembski.lab05.dto.TechnologyCreateDTO;
import dominik.dembski.lab05.dto.TechnologyDTO;
import dominik.dembski.lab05.mapper.EntityMapper;
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
    private final EntityMapper entityMapper;

    @PostMapping
    public ResponseEntity<TechnologyDTO> addTechnology(@RequestBody TechnologyCreateDTO technologyCreateDTO) {
        Technology technology = entityMapper.toTechnologyEntity(technologyCreateDTO);
        Technology savedTechnology = technologyService.addTechnology(technology);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityMapper.toTechnologyDTO(savedTechnology));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnologyDTO> getTechnologyById(@PathVariable UUID id) {
        return technologyService.getTechnologyById(id)
                .map(technology -> ResponseEntity.ok(entityMapper.toTechnologyDTO(technology)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TechnologyDTO>> getAllTechnologies() {
        return ResponseEntity.ok(entityMapper.toTechnologyDTOList(technologyService.getAllTechnologies()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnologyDTO> updateTechnology(@PathVariable UUID id, @RequestBody TechnologyCreateDTO technologyCreateDTO) {
        Technology technologyDetails = entityMapper.toTechnologyEntity(technologyCreateDTO);
        Technology updatedTechnology = technologyService.updateTechnology(id, technologyDetails);
        if (updatedTechnology != null) {
            return ResponseEntity.ok(entityMapper.toTechnologyDTO(updatedTechnology));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnologyById(@PathVariable UUID id) {
        technologyService.deleteTechnologyById(id);
        return ResponseEntity.noContent().build();
    }
}

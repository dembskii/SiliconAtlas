package dominik.dembski.lab05.service;

import dominik.dembski.lab05.domain.Technology;
import dominik.dembski.lab05.dto.TechnologyCreateDTO;
import dominik.dembski.lab05.dto.TechnologyDTO;
import dominik.dembski.lab05.mapper.EntityMapper;
import dominik.dembski.lab05.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TechnologyService {

    private final TechnologyRepository technologyRepository;
    private final EntityMapper entityMapper;

    // =====================================================
    // METODY DTO - dla REST API
    // =====================================================

    public TechnologyDTO addTechnology(TechnologyCreateDTO technologyCreateDTO) {
        Technology technology = entityMapper.toTechnologyEntity(technologyCreateDTO);
        Technology savedTechnology = technologyRepository.save(technology);
        return entityMapper.toTechnologyDTO(savedTechnology);
    }

    public Optional<TechnologyDTO> getTechnologyById(UUID id) {
        return technologyRepository.findById(id).map(entityMapper::toTechnologyDTO);
    }

    public List<TechnologyDTO> getAllTechnologies() {
        List<Technology> technologies = new ArrayList<>();
        technologyRepository.findAll().forEach(technologies::add);
        return entityMapper.toTechnologyDTOList(technologies);
    }

    public void deleteTechnologyById(UUID id) {
        technologyRepository.deleteById(id);
    }

    public TechnologyDTO updateTechnology(UUID id, TechnologyCreateDTO technologyCreateDTO) {
        Optional<Technology> technologyOpt = technologyRepository.findById(id);
        if (technologyOpt.isPresent()) {
            Technology existingTechnology = technologyOpt.get();
            if (technologyCreateDTO.getName() != null) {
                existingTechnology.setName(technologyCreateDTO.getName());
            }
            if (technologyCreateDTO.getDescription() != null) {
                existingTechnology.setDescription(technologyCreateDTO.getDescription());
            }
            if (technologyCreateDTO.getReleaseYear() > 0) {
                existingTechnology.setReleaseYear(technologyCreateDTO.getReleaseYear());
            }
            Technology savedTechnology = technologyRepository.save(existingTechnology);
            return entityMapper.toTechnologyDTO(savedTechnology);
        }
        return null;
    }

    // =====================================================
    // METODY WEWNĘTRZNE (encje) - dla AdminController/Thymeleaf
    // =====================================================

    public Technology addTechnologyEntity(Technology technology) {
        return technologyRepository.save(technology);
    }

    public Optional<Technology> getTechnologyEntityById(UUID id) {
        return technologyRepository.findById(id);
    }

    public List<Technology> getAllTechnologyEntities() {
        List<Technology> technologies = new ArrayList<>();
        technologyRepository.findAll().forEach(technologies::add);
        return technologies;
    }
}

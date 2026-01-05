package dominik.dembski.lab05.service;

import dominik.dembski.lab05.domain.Technology;
import dominik.dembski.lab05.repository.TechnologyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    public TechnologyService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    public Technology addTechnology(Technology technology) {
        return technologyRepository.save(technology);
    }

    public Optional<Technology> getTechnologyById(UUID id) {
        return technologyRepository.findById(id);
    }

    public List<Technology> getAllTechnologies() {
        List<Technology> result = new ArrayList<>();
        technologyRepository.findAll().forEach(result::add);
        return result;
    }

    public void deleteTechnologyById(UUID id) {
        technologyRepository.deleteById(id);
    }

    public Technology updateTechnology(UUID id, Technology technologyDetails) {
        Optional<Technology> technology = technologyRepository.findById(id);
        if (technology.isPresent()) {
            Technology existingTechnology = technology.get();
            if (technologyDetails.getName() != null) {
                existingTechnology.setName(technologyDetails.getName());
            }
            if (technologyDetails.getDescription() != null) {
                existingTechnology.setDescription(technologyDetails.getDescription());
            }
            if (technologyDetails.getReleaseYear() > 0) {
                existingTechnology.setReleaseYear(technologyDetails.getReleaseYear());
            }
            return technologyRepository.save(existingTechnology);
        }
        return null;
    }
}

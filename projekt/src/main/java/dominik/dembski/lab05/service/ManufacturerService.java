package dominik.dembski.lab05.service;

import dominik.dembski.lab05.domain.Manufacturer;
import dominik.dembski.lab05.dto.ManufacturerCreateDTO;
import dominik.dembski.lab05.dto.ManufacturerDTO;
import dominik.dembski.lab05.mapper.EntityMapper;
import dominik.dembski.lab05.repository.ManufacturerRepository;
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
public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    private final EntityMapper entityMapper;

    // =====================================================
    // METODY DTO - dla REST API
    // =====================================================

    public ManufacturerDTO addManufacturer(ManufacturerCreateDTO manufacturerCreateDTO) {
        Manufacturer manufacturer = entityMapper.toManufacturerEntity(manufacturerCreateDTO);
        Manufacturer savedManufacturer = manufacturerRepository.save(manufacturer);
        return entityMapper.toManufacturerDTO(savedManufacturer);
    }

    public Optional<ManufacturerDTO> getManufacturerById(UUID id) {
        return manufacturerRepository.findById(id).map(entityMapper::toManufacturerDTO);
    }

    public List<ManufacturerDTO> getAllManufacturers() {
        List<Manufacturer> manufacturers = new ArrayList<>();
        manufacturerRepository.findAll().forEach(manufacturers::add);
        return entityMapper.toManufacturerDTOList(manufacturers);
    }

    public void deleteManufacturerById(UUID id) {
        manufacturerRepository.deleteById(id);
    }

    public ManufacturerDTO updateManufacturer(UUID id, ManufacturerCreateDTO manufacturerCreateDTO) {
        Optional<Manufacturer> manufacturerOpt = manufacturerRepository.findById(id);
        if (manufacturerOpt.isPresent()) {
            Manufacturer existingManufacturer = manufacturerOpt.get();
            if (manufacturerCreateDTO.getName() != null) {
                existingManufacturer.setName(manufacturerCreateDTO.getName());
            }
            if (manufacturerCreateDTO.getCountry() != null) {
                existingManufacturer.setCountry(manufacturerCreateDTO.getCountry());
            }
            if (manufacturerCreateDTO.getFoundedYear() > 0) {
                existingManufacturer.setFoundedYear(manufacturerCreateDTO.getFoundedYear());
            }
            Manufacturer savedManufacturer = manufacturerRepository.save(existingManufacturer);
            return entityMapper.toManufacturerDTO(savedManufacturer);
        }
        return null;
    }

    // =====================================================
    // METODY WEWNĘTRZNE (encje) - dla AdminController/Thymeleaf
    // =====================================================

    public Manufacturer addManufacturerEntity(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    public Optional<Manufacturer> getManufacturerEntityById(UUID id) {
        return manufacturerRepository.findById(id);
    }

    public List<Manufacturer> getAllManufacturerEntities() {
        List<Manufacturer> manufacturers = new ArrayList<>();
        manufacturerRepository.findAll().forEach(manufacturers::add);
        return manufacturers;
    }
}

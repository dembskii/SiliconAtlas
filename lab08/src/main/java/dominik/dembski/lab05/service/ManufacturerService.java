package dominik.dembski.lab05.service;

import dominik.dembski.lab05.domain.Manufacturer;
import dominik.dembski.lab05.repository.ManufacturerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerService(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    public Manufacturer addManufacturer(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    public Optional<Manufacturer> getManufacturerById(UUID id) {
        return manufacturerRepository.findById(id);
    }

    public List<Manufacturer> getAllManufacturers() {
        List<Manufacturer> result = new ArrayList<>();
        manufacturerRepository.findAll().forEach(result::add);
        return result;
    }

    public void deleteManufacturerById(UUID id) {
        manufacturerRepository.deleteById(id);
    }

    public Manufacturer updateManufacturer(UUID id, Manufacturer manufacturerDetails) {
        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(id);
        if (manufacturer.isPresent()) {
            Manufacturer existingManufacturer = manufacturer.get();
            if (manufacturerDetails.getName() != null) {
                existingManufacturer.setName(manufacturerDetails.getName());
            }
            if (manufacturerDetails.getCountry() != null) {
                existingManufacturer.setCountry(manufacturerDetails.getCountry());
            }
            if (manufacturerDetails.getFoundedYear() > 0) {
                existingManufacturer.setFoundedYear(manufacturerDetails.getFoundedYear());
            }
            return manufacturerRepository.save(existingManufacturer);
        }
        return null;
    }
}

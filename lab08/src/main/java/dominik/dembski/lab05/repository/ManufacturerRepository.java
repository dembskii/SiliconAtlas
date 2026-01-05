package dominik.dembski.lab05.repository;

import dominik.dembski.lab05.domain.Manufacturer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ManufacturerRepository extends CrudRepository<Manufacturer, UUID> {
}

package dominik.dembski.lab05.repository;

import dominik.dembski.lab05.domain.Technology;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TechnologyRepository extends CrudRepository<Technology, UUID> {
}

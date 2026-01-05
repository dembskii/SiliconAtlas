package dominik.dembski.lab05.repository;

import dominik.dembski.lab05.domain.CpuSpecification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CpuSpecificationRepository extends CrudRepository<CpuSpecification, UUID> {
}

package dominik.dembski.lab05.repository;

import dominik.dembski.lab05.domain.Cpu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CpuRepository extends CrudRepository<Cpu, UUID> {
}

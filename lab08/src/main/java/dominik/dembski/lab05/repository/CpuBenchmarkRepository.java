package dominik.dembski.lab05.repository;

import dominik.dembski.lab05.domain.CpuBenchmark;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CpuBenchmarkRepository extends CrudRepository<CpuBenchmark, UUID> {
}

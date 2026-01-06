package dominik.dembski.lab05.repository;

import dominik.dembski.lab05.domain.CpuSpecification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CpuSpecificationRepository extends CrudRepository<CpuSpecification, UUID> {
    
    List<CpuSpecification> findBySocketType(String socketType);
    
    List<CpuSpecification> findByTdpWattsLessThan(int tdp);
    
    @Query("SELECT s FROM CpuSpecification s WHERE s.cacheL3MB >= :minCache")
    List<CpuSpecification> findByMinimumL3Cache(@Param("minCache") int minCache);
    
    @Query("SELECT s FROM CpuSpecification s WHERE s.tdpWatts <= :maxTdp AND s.cacheL3MB >= :minCache ORDER BY s.cacheL3MB DESC")
    List<CpuSpecification> findOptimalSpecs(@Param("maxTdp") int maxTdp, @Param("minCache") int minCache);
}


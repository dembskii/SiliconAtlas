package dominik.dembski.lab05.repository;

import dominik.dembski.lab05.domain.Manufacturer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ManufacturerRepository extends CrudRepository<Manufacturer, UUID> {
    
    List<Manufacturer> findByCountry(String country);
    
    List<Manufacturer> findByNameContaining(String name);
    
    @Query("SELECT m FROM Manufacturer m WHERE m.foundedYear >= :year ORDER BY m.foundedYear DESC")
    List<Manufacturer> findByFoundedYearGreaterThanOrEqual(@Param("year") int year);
    
    @Query("SELECT m FROM Manufacturer m WHERE SIZE(m.cpus) > :minCpuCount")
    List<Manufacturer> findManufacturersWithMultipleCpus(@Param("minCpuCount") int minCpuCount);
}


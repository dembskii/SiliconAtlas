package dominik.dembski.lab05.repository;

import dominik.dembski.lab05.domain.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, UUID> {
    
    // =====================================================
    // METODY findBy (konwencja nazewnicza)
    // =====================================================
    
    Optional<Manufacturer> findByName(String name);
    
    List<Manufacturer> findByCountry(String country);
    
    // =====================================================
    // ZAPYTANIE JPQL z JOIN
    // =====================================================
    
    @Query("SELECT DISTINCT m FROM Manufacturer m " +
           "JOIN m.cpus c " +
           "WHERE c.cores >= :minCores")
    List<Manufacturer> findManufacturersWithHighCoreCpus(@Param("minCores") int minCores);
    
    // =====================================================
    // ZAPYTANIE JPQL z GROUP BY i agregacją
    // =====================================================
    
    @Query("SELECT m.country, COUNT(c) " +
           "FROM Manufacturer m " +
           "JOIN m.cpus c " +
           "GROUP BY m.country " +
           "ORDER BY COUNT(c) DESC")
    List<Object[]> getCpuCountByCountry();
    
    // =====================================================
    // NATIVE QUERY
    // =====================================================
    
    @Query(value = "SELECT m.id, m.name, m.country, m.founded_year, " +
                   "COUNT(c.id) as cpu_count " +
                   "FROM manufacturer m " +
                   "LEFT JOIN cpu c ON c.manufacturer_id = m.id " +
                   "GROUP BY m.id, m.name, m.country, m.founded_year " +
                   "ORDER BY cpu_count DESC",
           nativeQuery = true)
    List<Object[]> getFullManufacturerStatsNative();
    
    @Query(value = "SELECT m.name, m.country, " +
                   "COUNT(DISTINCT c.id) as cpu_count, " +
                   "AVG(b.passmark_score) as avg_passmark " +
                   "FROM manufacturer m " +
                   "INNER JOIN cpu c ON c.manufacturer_id = m.id " +
                   "INNER JOIN cpu_benchmark b ON b.cpu_id = c.id " +
                   "GROUP BY m.name, m.country " +
                   "ORDER BY avg_passmark DESC",
           nativeQuery = true)
    List<Object[]> getManufacturerBenchmarkStatsNative();
}


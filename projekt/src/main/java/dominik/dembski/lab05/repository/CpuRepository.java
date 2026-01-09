package dominik.dembski.lab05.repository;

import dominik.dembski.lab05.domain.Cpu;
import dominik.dembski.lab05.dto.ManufacturerStatsDTO;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CpuRepository extends CrudRepository<Cpu, UUID>, 
                                        PagingAndSortingRepository<Cpu, UUID>,
                                        JpaSpecificationExecutor<Cpu> {
    
    // =====================================================
    // METODY findBy (konwencja nazewnicza)
    // =====================================================
    
    List<Cpu> findByManufacturerId(UUID manufacturerId);
    
    List<Cpu> findByCoresGreaterThanEqualAndFrequencyGhzGreaterThanEqual(int minCores, double minFrequency);
    
    // =====================================================
    // ZAPYTANIE JPQL z JOIN
    // =====================================================
    
    @Query("SELECT DISTINCT c FROM Cpu c " +
           "JOIN FETCH c.manufacturer m " +
           "LEFT JOIN FETCH c.benchmarks b " +
           "WHERE m.name = :manufacturerName")
    List<Cpu> findCpusWithBenchmarksByManufacturer(@Param("manufacturerName") String manufacturerName);
    
    // =====================================================
    // ZAPYTANIE JPQL z GROUP BY i agregacją
    // =====================================================
    
    @Query("SELECT new dominik.dembski.lab05.dto.ManufacturerStatsDTO(" +
           "m.name, COUNT(c), AVG(c.cores), AVG(c.frequencyGhz)) " +
           "FROM Cpu c JOIN c.manufacturer m " +
           "GROUP BY m.name " +
           "ORDER BY COUNT(c) DESC")
    List<ManufacturerStatsDTO> getManufacturerStatistics();
    
    // =====================================================
    // NATIVE QUERY
    // =====================================================
    
    @Query(value = "SELECT m.name as manufacturer_name, " +
                   "COUNT(c.id) as cpu_count, " +
                   "AVG(c.cores) as avg_cores, " +
                   "MAX(c.frequency_ghz) as max_frequency " +
                   "FROM cpu c " +
                   "INNER JOIN manufacturer m ON c.manufacturer_id = m.id " +
                   "GROUP BY m.name " +
                   "ORDER BY cpu_count DESC",
           nativeQuery = true)
    List<Object[]> getManufacturerStatsNative();
    
    @Query(value = "SELECT c.id, c.model, m.name as manufacturer, " +
                   "AVG(b.passmark_score) as avg_passmark " +
                   "FROM cpu c " +
                   "INNER JOIN manufacturer m ON c.manufacturer_id = m.id " +
                   "INNER JOIN cpu_benchmark b ON b.cpu_id = c.id " +
                   "GROUP BY c.id, c.model, m.name " +
                   "HAVING AVG(b.passmark_score) >= :minPassmark " +
                   "ORDER BY avg_passmark DESC",
           nativeQuery = true)
    List<Object[]> findCpusWithHighBenchmarksNative(@Param("minPassmark") double minPassmark);
}


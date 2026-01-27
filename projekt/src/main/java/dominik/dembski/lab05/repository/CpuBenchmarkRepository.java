package dominik.dembski.lab05.repository;

import dominik.dembski.lab05.domain.CpuBenchmark;
import dominik.dembski.lab05.dto.BenchmarkStatsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CpuBenchmarkRepository extends JpaRepository<CpuBenchmark, UUID> {
    
    // =====================================================
    // METODY findBy (konwencja nazewnicza)
    // =====================================================
    
    List<CpuBenchmark> findByCpuId(UUID cpuId);
    
    List<CpuBenchmark> findByPassmarkScoreGreaterThanOrderByPassmarkScoreDesc(int minScore);
    
    // =====================================================
    // ZAPYTANIE JPQL z JOIN
    // =====================================================
    
    @Query("SELECT b FROM CpuBenchmark b " +
           "JOIN b.cpu c " +
           "JOIN c.manufacturer m " +
           "WHERE m.name = :manufacturerName " +
           "ORDER BY b.passmarkScore DESC")
    List<CpuBenchmark> findBenchmarksByManufacturer(@Param("manufacturerName") String manufacturerName);
    
    // =====================================================
    // ZAPYTANIE JPQL z GROUP BY i agregacją
    // =====================================================
    
    @Query("SELECT new dominik.dembski.lab05.dto.BenchmarkStatsDTO(" +
           "c.model, m.name, COUNT(b), AVG(b.singleCoreScore), AVG(b.multiCoreScore), " +
           "AVG(b.passmarkScore), AVG(b.cinebenchR23)) " +
           "FROM CpuBenchmark b " +
           "JOIN b.cpu c " +
           "JOIN c.manufacturer m " +
           "GROUP BY c.model, m.name " +
           "ORDER BY AVG(b.passmarkScore) DESC")
    List<BenchmarkStatsDTO> getBenchmarkStatsByCpu();
    
    // =====================================================
    // NATIVE QUERY
    // =====================================================
    
    @Query(value = "SELECT c.model, m.name as manufacturer, " +
                   "COUNT(b.id) as test_count, " +
                   "AVG(b.single_core_score) as avg_single, " +
                   "AVG(b.multi_core_score) as avg_multi " +
                   "FROM cpu_benchmark b " +
                   "INNER JOIN cpu c ON b.cpu_id = c.id " +
                   "INNER JOIN manufacturer m ON c.manufacturer_id = m.id " +
                   "GROUP BY c.model, m.name " +
                   "ORDER BY avg_multi DESC",
           nativeQuery = true)
    List<Object[]> getDetailedBenchmarkStatsNative();
    
    @Query(value = "SELECT c.model, " +
                   "AVG(b.single_core_score) as avg_single, " +
                   "AVG(b.multi_core_score) as avg_multi, " +
                   "(AVG(b.multi_core_score) * 1.0 / NULLIF(AVG(b.single_core_score), 0)) as scaling_factor " +
                   "FROM cpu_benchmark b " +
                   "INNER JOIN cpu c ON b.cpu_id = c.id " +
                   "GROUP BY c.model " +
                   "ORDER BY scaling_factor DESC NULLS LAST",
           nativeQuery = true)
    List<Object[]> getCpuScalingFactorNative();
}


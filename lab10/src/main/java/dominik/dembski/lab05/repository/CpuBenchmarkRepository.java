package dominik.dembski.lab05.repository;

import dominik.dembski.lab05.domain.CpuBenchmark;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CpuBenchmarkRepository extends CrudRepository<CpuBenchmark, UUID> {
    
    List<CpuBenchmark> findBySingleCoreScoreGreaterThan(int score);
    
    List<CpuBenchmark> findByMultiCoreScoreAndPassmarkScore(int multiCore, int passmark);
    
    @Query("SELECT b FROM CpuBenchmark b WHERE b.cinebenchR23 > :minScore ORDER BY b.cinebenchR23 DESC")
    List<CpuBenchmark> findBestPerformingBenchmarks(@Param("minScore") double minScore);
    
    @Query("SELECT b FROM CpuBenchmark b WHERE b.cpu.id = :cpuId")
    List<CpuBenchmark> findBenchmarksByCpuId(@Param("cpuId") UUID cpuId);
}


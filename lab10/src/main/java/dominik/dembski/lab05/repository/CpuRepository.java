package dominik.dembski.lab05.repository;

import dominik.dembski.lab05.domain.Cpu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CpuRepository extends CrudRepository<Cpu, UUID> {
    
    // Metody findBy z And/Or
    List<Cpu> findByCoresAndThreads(int cores, int threads);
    
    List<Cpu> findByModelContainingOrManufacturerName(String model, String manufacturerName);
    
    List<Cpu> findByFrequencyGhzGreaterThan(double frequency);
    
    // Metody JPQL
    @Query("SELECT c FROM Cpu c WHERE c.cores >= :minCores AND c.threads >= :minThreads")
    List<Cpu> findByCoresAndThreadsGreaterThan(@Param("minCores") int minCores, @Param("minThreads") int minThreads);
    
    @Query("SELECT c FROM Cpu c WHERE LOWER(c.model) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Cpu> searchByModel(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT c FROM Cpu c JOIN c.manufacturer m WHERE m.name = :manufacturerName ORDER BY c.frequencyGhz DESC")
    List<Cpu> findByManufacturerNameOrdered(@Param("manufacturerName") String manufacturerName);
    
    @Query("SELECT c FROM Cpu c WHERE c.specification IS NOT NULL")
    List<Cpu> findCpusWithSpecifications();
}


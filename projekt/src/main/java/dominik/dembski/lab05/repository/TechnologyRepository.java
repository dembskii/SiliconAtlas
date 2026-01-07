package dominik.dembski.lab05.repository;

import dominik.dembski.lab05.domain.Technology;
import dominik.dembski.lab05.dto.TechnologyUsageDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TechnologyRepository extends CrudRepository<Technology, UUID> {
    
    // =====================================================
    // METODY findBy (konwencja nazewnicza)
    // =====================================================
    
    Optional<Technology> findByName(String name);
    
    List<Technology> findByReleaseYearGreaterThanEqual(int year);
    
    // =====================================================
    // ZAPYTANIE JPQL z JOIN
    // =====================================================
    
    @Query("SELECT t FROM Technology t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Technology> searchByDescription(@Param("keyword") String keyword);
    
    // =====================================================
    // ZAPYTANIE JPQL z GROUP BY i agregacją
    // =====================================================
    
    @Query("SELECT new dominik.dembski.lab05.dto.TechnologyUsageDTO(" +
           "t.id, t.name, t.releaseYear, COUNT(c)) " +
           "FROM Technology t " +
           "LEFT JOIN t.cpus c " +
           "GROUP BY t.id, t.name, t.releaseYear " +
           "ORDER BY COUNT(c) DESC")
    List<TechnologyUsageDTO> getTechnologyUsageStats();
    
    // =====================================================
    // NATIVE QUERY
    // =====================================================
    
    @Query(value = "SELECT t.id, t.name, t.description, t.release_year, " +
                   "COUNT(ct.cpu_id) as cpu_count " +
                   "FROM technology t " +
                   "LEFT JOIN cpu_technology ct ON t.id = ct.technology_id " +
                   "GROUP BY t.id, t.name, t.description, t.release_year " +
                   "ORDER BY cpu_count DESC",
           nativeQuery = true)
    List<Object[]> getFullTechnologyStatsNative();
    
    @Query(value = "SELECT t.name as technology, m.name as manufacturer, " +
                   "COUNT(c.id) as cpu_count " +
                   "FROM technology t " +
                   "INNER JOIN cpu_technology ct ON t.id = ct.technology_id " +
                   "INNER JOIN cpu c ON ct.cpu_id = c.id " +
                   "INNER JOIN manufacturer m ON c.manufacturer_id = m.id " +
                   "GROUP BY t.name, m.name " +
                   "ORDER BY t.name, cpu_count DESC",
           nativeQuery = true)
    List<Object[]> getTechnologyUsageByManufacturerNative();
}


package dominik.dembski.lab05.repository;

import dominik.dembski.lab05.domain.Technology;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TechnologyRepository extends CrudRepository<Technology, UUID> {
    
    List<Technology> findByReleaseYear(int year);
    
    List<Technology> findByNameContaining(String name);
    
    @Query("SELECT t FROM Technology t WHERE t.releaseYear >= :year ORDER BY t.releaseYear DESC")
    List<Technology> findRecentTechnologies(@Param("year") int year);
    
    @Query("SELECT t FROM Technology t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Technology> searchByDescription(@Param("keyword") String keyword);
}


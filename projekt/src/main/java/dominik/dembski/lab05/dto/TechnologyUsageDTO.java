package dominik.dembski.lab05.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dla statystyk użycia technologii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyUsageDTO {
    
    private UUID technologyId;
    private String technologyName;
    private int releaseYear;
    private Long cpuCount;
    private Double avgCpuCores;
    private Double avgCpuFrequency;
}

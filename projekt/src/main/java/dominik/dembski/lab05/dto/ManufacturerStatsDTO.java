package dominik.dembski.lab05.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dla statystyk producenta - używane w zapytaniach agregujących
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturerStatsDTO {
    
    private String manufacturerName;
    private Long cpuCount;
    private Double avgCores;
    private Double avgFrequency;
    private Double avgBenchmarkScore;
}

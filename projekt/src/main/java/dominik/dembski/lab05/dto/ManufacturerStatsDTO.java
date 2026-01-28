package dominik.dembski.lab05.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dla statystyk producenta - używane w zapytaniach agregujących
 */
@Data
@NoArgsConstructor
public class ManufacturerStatsDTO {
    
    private String manufacturerName;
    private Long cpuCount;
    private Double avgCores;
    private Double avgFrequency;
    private Double avgBenchmarkScore;

    // Constructor for JPQL queries
    public ManufacturerStatsDTO(String manufacturerName, Long cpuCount, Double avgCores, Double avgFrequency, Double avgBenchmarkScore) {
        this.manufacturerName = manufacturerName;
        this.cpuCount = cpuCount;
        this.avgCores = avgCores;
        this.avgFrequency = avgFrequency;
        this.avgBenchmarkScore = avgBenchmarkScore;
    }
}

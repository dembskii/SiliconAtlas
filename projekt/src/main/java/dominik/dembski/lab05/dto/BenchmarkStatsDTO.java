package dominik.dembski.lab05.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dla statystyk benchmarków
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkStatsDTO {
    
    private String cpuModel;
    private String manufacturerName;
    private Long benchmarkCount;
    private Double avgSingleCore;
    private Double avgMultiCore;
    private Double avgPassmark;
    private Double avgCinebench;
    private Integer maxSingleCore;
    private Integer maxMultiCore;
    private Integer minSingleCore;
    private Integer minMultiCore;
}

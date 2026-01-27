package dominik.dembski.lab05.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dla wydajności CPU - łączy dane CPU z benchmarkami
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CpuPerformanceDTO {
    
    private UUID cpuId;
    private String cpuModel;
    private String manufacturerName;
    private int cores;
    private int threads;
    private double frequencyGhz;
    private Double avgSingleCoreScore;
    private Double avgMultiCoreScore;
    private Double avgPassmarkScore;
    private Double maxCinebenchR23;
    private Long benchmarkCount;
    private Double performancePerWatt;
}

package com.cpu.management.dto;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dla statystyk użycia technologii
 */
@Data
@NoArgsConstructor
public class TechnologyUsageDTO {
    
    private UUID technologyId;
    private String technologyName;
    private int releaseYear;
    private Long cpuCount;
    private Double avgCpuCores;
    private Double avgCpuFrequency;

    // Constructor for JPQL queries (getTechnologyUsageStats)
    public TechnologyUsageDTO(UUID technologyId, String technologyName, int releaseYear, Long cpuCount) {
        this.technologyId = technologyId;
        this.technologyName = technologyName;
        this.releaseYear = releaseYear;
        this.cpuCount = cpuCount;
    }
}

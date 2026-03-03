package com.cpu.management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dla statystyk benchmarków
 */
@Data
@NoArgsConstructor
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

    // Constructor for JPQL queries
    public BenchmarkStatsDTO(String cpuModel, String manufacturerName, Long benchmarkCount,
                             Double avgSingleCore, Double avgMultiCore, Double avgPassmark, Double avgCinebench) {
        this.cpuModel = cpuModel;
        this.manufacturerName = manufacturerName;
        this.benchmarkCount = benchmarkCount;
        this.avgSingleCore = avgSingleCore;
        this.avgMultiCore = avgMultiCore;
        this.avgPassmark = avgPassmark;
        this.avgCinebench = avgCinebench;
    }
}


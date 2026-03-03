package com.cpu.management.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dla porównania dwóch procesorów
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CpuComparisonDTO {
    
    private CpuDetailsDTO cpu1;
    private CpuDetailsDTO cpu2;
    private String winner;
    private String comparisonSummary;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CpuDetailsDTO {
        private UUID id;
        private String model;
        private String manufacturer;
        private int cores;
        private int threads;
        private double frequencyGhz;
        private Integer tdpWatts;
        private Double avgBenchmarkScore;
        private int benchmarkWins;
    }
}

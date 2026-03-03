package com.cpu.management.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CpuBenchmarkDTO {
    private UUID id;
    private int singleCoreScore;
    private int multiCoreScore;
    private int passmarkScore;
    private double cinebenchR23;
    private String testDate;
    private UUID cpuId;
    private String cpuModel;
}

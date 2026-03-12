package com.cpu.management.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CpuAiAutofillResponseDTO {
    private String model;
    private int cores;
    private int threads;
    private double frequencyGhz;
    
    // Specification details
    private int cacheL1KB;
    private int cacheL2KB;
    private int cacheL3MB;
    private int tdpWatts;
    private String socketType;
    
    // Manufacturer
    private String manufacturerName;
    
    // Technologies
    private List<String> technologies;
}

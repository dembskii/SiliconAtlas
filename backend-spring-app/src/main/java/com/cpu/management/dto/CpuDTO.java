package com.cpu.management.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CpuDTO {
    private UUID id;
    private String model;
    private int cores;
    private int threads;
    private double frequencyGhz;
    private String manufacturerName;
    private UUID manufacturerId;
    private List<String> technologyNames;
    private List<UUID> technologyIds;
    private CpuSpecificationDTO specification;
}

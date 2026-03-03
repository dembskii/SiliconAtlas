package com.cpu.management.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CpuCreateDTO {
    private String model;
    private int cores;
    private int threads;
    private double frequencyGhz;
    private UUID manufacturerId;
    private List<UUID> technologyIds;
}

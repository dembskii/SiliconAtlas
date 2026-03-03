package com.cpu.management.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnologyDTO {
    private UUID id;
    private String name;
    private String description;
    private int releaseYear;
    private int cpuCount;
}

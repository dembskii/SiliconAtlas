package com.cpu.management.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO dla eventów związanych z CPU
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CpuEventDTO {
    private UUID eventId;
    private String eventType;
    private UUID cpuId;
    private String cpuModel;
    private String manufacturer;
    private Integer cores;
    private Integer threads;
    private Double baseFrequency;
    private LocalDateTime timestamp;
    private String userId;
    private String details;
}

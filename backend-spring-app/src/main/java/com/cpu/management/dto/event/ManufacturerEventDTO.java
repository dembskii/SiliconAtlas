package com.cpu.management.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManufacturerEventDTO {
    private UUID eventId;
    private String eventType;
    private UUID manufacturerId;
    private String manufacturerName;
    private String country;
    private Integer foundedYear;
    private LocalDateTime timestamp;
    private String userId;
    private String details;
}

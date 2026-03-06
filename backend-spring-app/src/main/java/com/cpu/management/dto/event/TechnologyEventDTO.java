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
public class TechnologyEventDTO {
    private UUID eventId;
    private String eventType;
    private UUID technologyId;
    private String technologyName;
    private String description;
    private Integer releaseYear;
    private LocalDateTime timestamp;
    private String userId;
    private String details;
}

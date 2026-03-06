package com.cpu.management.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class DomainEventDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID eventId;
    private String eventType;
    private String entityType;
    private UUID entityId;
    private LocalDateTime timestamp;
    private String userId;
}

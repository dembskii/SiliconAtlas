package com.cpu.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiResponseDTO {
    private String response;
    private String prompt;
    private LocalDateTime timestamp;
    private String model;
    private Double temperature;
}

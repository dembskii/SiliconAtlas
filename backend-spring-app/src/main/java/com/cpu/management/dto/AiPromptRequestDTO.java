package com.cpu.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiPromptRequestDTO {
    private String message;
    private Double temperature;
    private Integer maxTokens;
}

package com.cpu.management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnologyCreateDTO {
    private String name;
    private String description;
    private int releaseYear;
}

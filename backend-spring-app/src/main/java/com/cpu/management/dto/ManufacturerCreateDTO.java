package com.cpu.management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManufacturerCreateDTO {
    private String name;
    private String country;
    private int foundedYear;
}

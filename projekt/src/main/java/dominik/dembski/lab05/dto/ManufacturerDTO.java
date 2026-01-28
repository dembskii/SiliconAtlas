package dominik.dembski.lab05.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManufacturerDTO {
    private UUID id;
    private String name;
    private String country;
    private int foundedYear;
    private int cpuCount;
}

package dominik.dembski.lab05.dto;

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

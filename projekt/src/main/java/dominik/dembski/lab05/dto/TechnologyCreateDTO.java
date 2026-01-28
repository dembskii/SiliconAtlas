package dominik.dembski.lab05.dto;

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

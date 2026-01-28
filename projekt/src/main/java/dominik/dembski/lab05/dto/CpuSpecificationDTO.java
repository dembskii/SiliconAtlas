package dominik.dembski.lab05.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CpuSpecificationDTO {
    private UUID id;
    private int cacheL1KB;
    private int cacheL2KB;
    private int cacheL3MB;
    private int tdpWatts;
    private String socketType;
}

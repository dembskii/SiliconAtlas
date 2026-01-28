package dominik.dembski.lab05.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CpuSpecificationCreateDTO {
    private int cacheL1KB;
    private int cacheL2KB;
    private int cacheL3MB;
    private int tdpWatts;
    private String socketType;
}

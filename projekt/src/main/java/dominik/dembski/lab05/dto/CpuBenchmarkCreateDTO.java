package dominik.dembski.lab05.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CpuBenchmarkCreateDTO {
    private int singleCoreScore;
    private int multiCoreScore;
    private int passmarkScore;
    private double cinebenchR23;
    private String testDate;
}

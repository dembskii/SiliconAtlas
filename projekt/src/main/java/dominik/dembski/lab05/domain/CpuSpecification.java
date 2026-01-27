package dominik.dembski.lab05.domain;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CpuSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int cacheL1KB;
    private int cacheL2KB;
    private int cacheL3MB;
    private int tdpWatts;
    private String socketType;

    public CpuSpecification(int cacheL1KB, int cacheL2KB, int cacheL3MB, int tdpWatts, String socketType) {
        this.cacheL1KB = cacheL1KB;
        this.cacheL2KB = cacheL2KB;
        this.cacheL3MB = cacheL3MB;
        this.tdpWatts = tdpWatts;
        this.socketType = socketType;
    }
}
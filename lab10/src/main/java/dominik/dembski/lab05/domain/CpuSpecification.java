package dominik.dembski.lab05.domain;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CpuSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int cacheL1KB;
    private int cacheL2KB;
    private int cacheL3MB;
    private int tdpWatts;
    private String socketType;
}
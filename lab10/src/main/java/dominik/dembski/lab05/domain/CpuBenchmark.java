package dominik.dembski.lab05.domain;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CpuBenchmark {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int singleCoreScore;
    private int multiCoreScore;
    private int passmarkScore;
    private double cinebenchR23;
    private String testDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cpu_id")
    private Cpu cpu;
}
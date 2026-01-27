package dominik.dembski.lab05.domain;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "cpu")
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
    private Cpu cpu;

    public CpuBenchmark(int singleCoreScore, int multiCoreScore, int passmarkScore, double cinebenchR23, String testDate) {
        this.singleCoreScore = singleCoreScore;
        this.multiCoreScore = multiCoreScore;
        this.passmarkScore = passmarkScore;
        this.cinebenchR23 = cinebenchR23;
        this.testDate = testDate;
    }
}
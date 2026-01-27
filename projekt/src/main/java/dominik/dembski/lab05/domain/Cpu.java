package dominik.dembski.lab05.domain;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"manufacturer", "specification", "benchmarks", "technologies"})
public class Cpu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String model;
    private int cores;
    private int threads;
    private double frequencyGhz;

    @ManyToOne(fetch = FetchType.EAGER)
    private Manufacturer manufacturer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private CpuSpecification specification;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cpu", orphanRemoval = true)
    private List<CpuBenchmark> benchmarks;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "cpu_technology")
    private List<Technology> technologies;

    public Cpu(String model, int cores, int threads, double frequencyGhz) {
        this.model = model;
        this.cores = cores;
        this.threads = threads;
        this.frequencyGhz = frequencyGhz;
    }
}
package dominik.dembski.lab05.domain;

import java.util.UUID;
import java.util.List;

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
public class Cpu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String model;
    private int cores;
    private int threads;
    private double frequencyGhz;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "specification_id")
    private CpuSpecification specification;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cpu", orphanRemoval = true)
    private List<CpuBenchmark> benchmarks;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "cpu_technology",
        joinColumns = @JoinColumn(name = "cpu_id"),
        inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private List<Technology> technologies;
}
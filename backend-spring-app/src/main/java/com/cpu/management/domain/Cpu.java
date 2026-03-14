package com.cpu.management.domain;

import java.util.List;
import java.util.UUID;

import com.cpu.management.annotation.PositiveValue;
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
    
    @PositiveValue(message = "Number of cores must be positive")
    private int cores;
    
    @PositiveValue(message = "Number of threads must be positive")
    private int threads;
    
    @PositiveValue(message = "Frequency must be positive")
    private double frequencyGhz;

    @ManyToOne(fetch = FetchType.EAGER)
    private Manufacturer manufacturer;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
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
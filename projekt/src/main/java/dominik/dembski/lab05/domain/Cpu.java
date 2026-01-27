package dominik.dembski.lab05.domain;

import java.util.UUID;
import java.util.List;

import jakarta.persistence.*;

@Entity
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

    public Cpu() {
    }

    public Cpu(String model, int cores, int threads, double frequencyGhz) {
        this.model = model;
        this.cores = cores;
        this.threads = threads;
        this.frequencyGhz = frequencyGhz;
    }

    public UUID getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCores() {
        return cores;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public double getFrequencyGhz() {
        return frequencyGhz;
    }

    public void setFrequencyGhz(double frequencyGhz) {
        this.frequencyGhz = frequencyGhz;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public CpuSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(CpuSpecification specification) {
        this.specification = specification;
    }

    public List<CpuBenchmark> getBenchmarks() {
        return benchmarks;
    }

    public void setBenchmarks(List<CpuBenchmark> benchmarks) {
        this.benchmarks = benchmarks;
    }

    public List<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Technology> technologies) {
        this.technologies = technologies;
    }

    @Override
    public String toString() {
        return "Cpu{" +
                "model='" + model + '\'' +
                ", cores=" + cores +
                ", threads=" + threads +
                ", frequencyGhz=" + frequencyGhz +
                '}';
    }
}
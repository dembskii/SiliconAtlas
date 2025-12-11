package dominik.dembski.lab05.domain;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Cpu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String model;
    private int cores;
    private int threads;
    private double frequencyGhz;


    @OneToOne
    @JoinColumn(name = "specification_id", referencedColumnName = "id")
    private CpuSpecification specification;

    @OneToOne
    @JoinColumn(name = "benchmark_id", referencedColumnName = "id")
    private CpuBenchmark benchmark;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

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

    public CpuSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(CpuSpecification specification) {
        this.specification = specification;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public CpuBenchmark getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(CpuBenchmark benchmark) {
        this.benchmark = benchmark;
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
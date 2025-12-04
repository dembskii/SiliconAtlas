package dominik.dembski.lab05.domain;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Cpu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private final UUID id;


    private String producer;
    private int cores;
    private int threads;
    private double frequencyGhz;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "specification_id", referencedColumnName = "id")
    private CpuSpecification specification;

    public Cpu() {
        this.id = UUID.randomUUID();
    }

    public Cpu(String model, int cores, int threads, double frequencyGhz) {
        this.id = UUID.randomUUID();
        this.producer = model;
        this.cores = cores;
        this.threads = threads;
        this.frequencyGhz = frequencyGhz;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public UUID getId() {
        return id;
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

    @Override
    public String toString() {
        return "Cpu{" +
                "producer='" + producer + '\'' +
                ", cores=" + cores +
                ", threads=" + threads +
                ", frequencyGhz=" + frequencyGhz +
                ", specification=" + specification +
                '}';
    }
}

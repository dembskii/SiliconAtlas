package ug.lab04.procesor.cpu;

import java.util.UUID;

public class Cpu {

    private final UUID id;
    private String producer;
    private int cores;
    private int threads;
    private double frequencyGhz;


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

    @Override
    public String toString() {
        return "Cpu{" +
                "producer='" + producer + '\'' +
                ", cores=" + cores +
                ", threads=" + threads +
                ", frequencyGhz=" + frequencyGhz +
                '}';
    }
}

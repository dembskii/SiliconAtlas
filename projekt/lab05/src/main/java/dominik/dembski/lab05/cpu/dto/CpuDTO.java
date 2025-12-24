package dominik.dembski.lab05.cpu.dto;

import java.util.UUID;

public class CpuDTO {

    private UUID id;
    private String model;
    private int cores;
    private int threads;
    private double frequencyGhz;

    public CpuDTO() {
    }

    public CpuDTO(UUID id, String model, int cores, int threads, double frequencyGhz) {
        this.id = id;
        this.model = model;
        this.cores = cores;
        this.threads = threads;
        this.frequencyGhz = frequencyGhz;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}

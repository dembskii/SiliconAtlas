package dominik.dembski.lab05.dto;

import java.util.UUID;

/**
 * DTO dla wydajności CPU - łączy dane CPU z benchmarkami
 */
public class CpuPerformanceDTO {
    
    private UUID cpuId;
    private String cpuModel;
    private String manufacturerName;
    private int cores;
    private int threads;
    private double frequencyGhz;
    private Double avgSingleCoreScore;
    private Double avgMultiCoreScore;
    private Double avgPassmarkScore;
    private Double maxCinebenchR23;
    private Long benchmarkCount;
    private Double performancePerWatt;

    public CpuPerformanceDTO() {
    }

    public CpuPerformanceDTO(UUID cpuId, String cpuModel, String manufacturerName, int cores, int threads,
                             double frequencyGhz, Double avgSingleCoreScore, Double avgMultiCoreScore,
                             Double avgPassmarkScore, Double maxCinebenchR23, Long benchmarkCount) {
        this.cpuId = cpuId;
        this.cpuModel = cpuModel;
        this.manufacturerName = manufacturerName;
        this.cores = cores;
        this.threads = threads;
        this.frequencyGhz = frequencyGhz;
        this.avgSingleCoreScore = avgSingleCoreScore;
        this.avgMultiCoreScore = avgMultiCoreScore;
        this.avgPassmarkScore = avgPassmarkScore;
        this.maxCinebenchR23 = maxCinebenchR23;
        this.benchmarkCount = benchmarkCount;
    }

    // Getters and Setters
    public UUID getCpuId() {
        return cpuId;
    }

    public void setCpuId(UUID cpuId) {
        this.cpuId = cpuId;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
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

    public Double getAvgSingleCoreScore() {
        return avgSingleCoreScore;
    }

    public void setAvgSingleCoreScore(Double avgSingleCoreScore) {
        this.avgSingleCoreScore = avgSingleCoreScore;
    }

    public Double getAvgMultiCoreScore() {
        return avgMultiCoreScore;
    }

    public void setAvgMultiCoreScore(Double avgMultiCoreScore) {
        this.avgMultiCoreScore = avgMultiCoreScore;
    }

    public Double getAvgPassmarkScore() {
        return avgPassmarkScore;
    }

    public void setAvgPassmarkScore(Double avgPassmarkScore) {
        this.avgPassmarkScore = avgPassmarkScore;
    }

    public Double getMaxCinebenchR23() {
        return maxCinebenchR23;
    }

    public void setMaxCinebenchR23(Double maxCinebenchR23) {
        this.maxCinebenchR23 = maxCinebenchR23;
    }

    public Long getBenchmarkCount() {
        return benchmarkCount;
    }

    public void setBenchmarkCount(Long benchmarkCount) {
        this.benchmarkCount = benchmarkCount;
    }

    public Double getPerformancePerWatt() {
        return performancePerWatt;
    }

    public void setPerformancePerWatt(Double performancePerWatt) {
        this.performancePerWatt = performancePerWatt;
    }
}

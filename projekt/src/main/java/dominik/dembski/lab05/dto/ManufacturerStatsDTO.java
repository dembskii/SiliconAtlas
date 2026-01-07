package dominik.dembski.lab05.dto;

/**
 * DTO dla statystyk producenta - używane w zapytaniach agregujących
 */
public class ManufacturerStatsDTO {
    
    private String manufacturerName;
    private Long cpuCount;
    private Double avgCores;
    private Double avgFrequency;
    private Double avgBenchmarkScore;

    public ManufacturerStatsDTO() {
    }

    public ManufacturerStatsDTO(String manufacturerName, Long cpuCount, Double avgCores, Double avgFrequency) {
        this.manufacturerName = manufacturerName;
        this.cpuCount = cpuCount;
        this.avgCores = avgCores;
        this.avgFrequency = avgFrequency;
    }

    public ManufacturerStatsDTO(String manufacturerName, Long cpuCount, Double avgCores, Double avgFrequency, Double avgBenchmarkScore) {
        this.manufacturerName = manufacturerName;
        this.cpuCount = cpuCount;
        this.avgCores = avgCores;
        this.avgFrequency = avgFrequency;
        this.avgBenchmarkScore = avgBenchmarkScore;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public Long getCpuCount() {
        return cpuCount;
    }

    public void setCpuCount(Long cpuCount) {
        this.cpuCount = cpuCount;
    }

    public Double getAvgCores() {
        return avgCores;
    }

    public void setAvgCores(Double avgCores) {
        this.avgCores = avgCores;
    }

    public Double getAvgFrequency() {
        return avgFrequency;
    }

    public void setAvgFrequency(Double avgFrequency) {
        this.avgFrequency = avgFrequency;
    }

    public Double getAvgBenchmarkScore() {
        return avgBenchmarkScore;
    }

    public void setAvgBenchmarkScore(Double avgBenchmarkScore) {
        this.avgBenchmarkScore = avgBenchmarkScore;
    }
}

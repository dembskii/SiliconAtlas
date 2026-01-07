package dominik.dembski.lab05.dto;

/**
 * DTO dla statystyk benchmarków
 */
public class BenchmarkStatsDTO {
    
    private String cpuModel;
    private String manufacturerName;
    private Long benchmarkCount;
    private Double avgSingleCore;
    private Double avgMultiCore;
    private Double avgPassmark;
    private Double avgCinebench;
    private Integer maxSingleCore;
    private Integer maxMultiCore;
    private Integer minSingleCore;
    private Integer minMultiCore;

    public BenchmarkStatsDTO() {
    }

    public BenchmarkStatsDTO(String cpuModel, String manufacturerName, Long benchmarkCount, 
                             Double avgSingleCore, Double avgMultiCore, Double avgPassmark, Double avgCinebench) {
        this.cpuModel = cpuModel;
        this.manufacturerName = manufacturerName;
        this.benchmarkCount = benchmarkCount;
        this.avgSingleCore = avgSingleCore;
        this.avgMultiCore = avgMultiCore;
        this.avgPassmark = avgPassmark;
        this.avgCinebench = avgCinebench;
    }

    // Getters and Setters
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

    public Long getBenchmarkCount() {
        return benchmarkCount;
    }

    public void setBenchmarkCount(Long benchmarkCount) {
        this.benchmarkCount = benchmarkCount;
    }

    public Double getAvgSingleCore() {
        return avgSingleCore;
    }

    public void setAvgSingleCore(Double avgSingleCore) {
        this.avgSingleCore = avgSingleCore;
    }

    public Double getAvgMultiCore() {
        return avgMultiCore;
    }

    public void setAvgMultiCore(Double avgMultiCore) {
        this.avgMultiCore = avgMultiCore;
    }

    public Double getAvgPassmark() {
        return avgPassmark;
    }

    public void setAvgPassmark(Double avgPassmark) {
        this.avgPassmark = avgPassmark;
    }

    public Double getAvgCinebench() {
        return avgCinebench;
    }

    public void setAvgCinebench(Double avgCinebench) {
        this.avgCinebench = avgCinebench;
    }

    public Integer getMaxSingleCore() {
        return maxSingleCore;
    }

    public void setMaxSingleCore(Integer maxSingleCore) {
        this.maxSingleCore = maxSingleCore;
    }

    public Integer getMaxMultiCore() {
        return maxMultiCore;
    }

    public void setMaxMultiCore(Integer maxMultiCore) {
        this.maxMultiCore = maxMultiCore;
    }

    public Integer getMinSingleCore() {
        return minSingleCore;
    }

    public void setMinSingleCore(Integer minSingleCore) {
        this.minSingleCore = minSingleCore;
    }

    public Integer getMinMultiCore() {
        return minMultiCore;
    }

    public void setMinMultiCore(Integer minMultiCore) {
        this.minMultiCore = minMultiCore;
    }
}

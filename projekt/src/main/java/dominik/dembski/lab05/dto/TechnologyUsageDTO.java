package dominik.dembski.lab05.dto;

import java.util.UUID;

/**
 * DTO dla statystyk użycia technologii
 */
public class TechnologyUsageDTO {
    
    private UUID technologyId;
    private String technologyName;
    private int releaseYear;
    private Long cpuCount;
    private Double avgCpuCores;
    private Double avgCpuFrequency;

    public TechnologyUsageDTO() {
    }

    public TechnologyUsageDTO(UUID technologyId, String technologyName, int releaseYear, Long cpuCount) {
        this.technologyId = technologyId;
        this.technologyName = technologyName;
        this.releaseYear = releaseYear;
        this.cpuCount = cpuCount;
    }

    public TechnologyUsageDTO(UUID technologyId, String technologyName, int releaseYear, Long cpuCount, 
                              Double avgCpuCores, Double avgCpuFrequency) {
        this.technologyId = technologyId;
        this.technologyName = technologyName;
        this.releaseYear = releaseYear;
        this.cpuCount = cpuCount;
        this.avgCpuCores = avgCpuCores;
        this.avgCpuFrequency = avgCpuFrequency;
    }

    // Getters and Setters
    public UUID getTechnologyId() {
        return technologyId;
    }

    public void setTechnologyId(UUID technologyId) {
        this.technologyId = technologyId;
    }

    public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
        this.technologyName = technologyName;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Long getCpuCount() {
        return cpuCount;
    }

    public void setCpuCount(Long cpuCount) {
        this.cpuCount = cpuCount;
    }

    public Double getAvgCpuCores() {
        return avgCpuCores;
    }

    public void setAvgCpuCores(Double avgCpuCores) {
        this.avgCpuCores = avgCpuCores;
    }

    public Double getAvgCpuFrequency() {
        return avgCpuFrequency;
    }

    public void setAvgCpuFrequency(Double avgCpuFrequency) {
        this.avgCpuFrequency = avgCpuFrequency;
    }
}

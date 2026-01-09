package dominik.dembski.lab05.dto;

/**
 * DTO zawierające kryteria wyszukiwania CPU.
 * Wszystkie pola są opcjonalne - null oznacza brak filtrowania po danym kryterium.
 */
public class CpuSearchCriteriaDTO {

    private String model;           // fragment nazwy modelu (LIKE)
    private String manufacturer;    // fragment nazwy producenta (LIKE)
    private Integer minCores;
    private Integer maxCores;
    private Integer minThreads;
    private Integer maxThreads;
    private Double minFrequency;
    private Double maxFrequency;
    private String technology;      // fragment nazwy technologii (LIKE)

    public CpuSearchCriteriaDTO() {
    }

    // Gettery i Settery

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getMinCores() {
        return minCores;
    }

    public void setMinCores(Integer minCores) {
        this.minCores = minCores;
    }

    public Integer getMaxCores() {
        return maxCores;
    }

    public void setMaxCores(Integer maxCores) {
        this.maxCores = maxCores;
    }

    public Integer getMinThreads() {
        return minThreads;
    }

    public void setMinThreads(Integer minThreads) {
        this.minThreads = minThreads;
    }

    public Integer getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(Integer maxThreads) {
        this.maxThreads = maxThreads;
    }

    public Double getMinFrequency() {
        return minFrequency;
    }

    public void setMinFrequency(Double minFrequency) {
        this.minFrequency = minFrequency;
    }

    public Double getMaxFrequency() {
        return maxFrequency;
    }

    public void setMaxFrequency(Double maxFrequency) {
        this.maxFrequency = maxFrequency;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }
}

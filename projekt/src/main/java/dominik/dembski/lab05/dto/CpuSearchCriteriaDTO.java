package dominik.dembski.lab05.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO zawierające kryteria wyszukiwania CPU.
 * Wszystkie pola są opcjonalne - null oznacza brak filtrowania po danym kryterium.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CpuSearchCriteriaDTO {

    private String model;   
    private String manufacturer;
    private Integer minCores;
    private Integer maxCores;
    private Integer minThreads;
    private Integer maxThreads;
    private Double minFrequency;
    private Double maxFrequency;
    private String technology;
}

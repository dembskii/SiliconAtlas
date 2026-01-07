package dominik.dembski.lab05.dto;

import java.util.UUID;

/**
 * DTO dla porównania dwóch procesorów
 */
public class CpuComparisonDTO {
    
    private CpuDetailsDTO cpu1;
    private CpuDetailsDTO cpu2;
    private String winner;
    private String comparisonSummary;

    public CpuComparisonDTO() {
    }

    public CpuComparisonDTO(CpuDetailsDTO cpu1, CpuDetailsDTO cpu2, String winner, String comparisonSummary) {
        this.cpu1 = cpu1;
        this.cpu2 = cpu2;
        this.winner = winner;
        this.comparisonSummary = comparisonSummary;
    }

    public CpuDetailsDTO getCpu1() {
        return cpu1;
    }

    public void setCpu1(CpuDetailsDTO cpu1) {
        this.cpu1 = cpu1;
    }

    public CpuDetailsDTO getCpu2() {
        return cpu2;
    }

    public void setCpu2(CpuDetailsDTO cpu2) {
        this.cpu2 = cpu2;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getComparisonSummary() {
        return comparisonSummary;
    }

    public void setComparisonSummary(String comparisonSummary) {
        this.comparisonSummary = comparisonSummary;
    }

    public static class CpuDetailsDTO {
        private UUID id;
        private String model;
        private String manufacturer;
        private int cores;
        private int threads;
        private double frequencyGhz;
        private Integer tdpWatts;
        private Double avgBenchmarkScore;
        private int benchmarkWins;

        public CpuDetailsDTO() {
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

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
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

        public Integer getTdpWatts() {
            return tdpWatts;
        }

        public void setTdpWatts(Integer tdpWatts) {
            this.tdpWatts = tdpWatts;
        }

        public Double getAvgBenchmarkScore() {
            return avgBenchmarkScore;
        }

        public void setAvgBenchmarkScore(Double avgBenchmarkScore) {
            this.avgBenchmarkScore = avgBenchmarkScore;
        }

        public int getBenchmarkWins() {
            return benchmarkWins;
        }

        public void setBenchmarkWins(int benchmarkWins) {
            this.benchmarkWins = benchmarkWins;
        }
    }
}

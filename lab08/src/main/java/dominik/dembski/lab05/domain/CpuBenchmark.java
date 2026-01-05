package dominik.dembski.lab05.domain;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CpuBenchmark {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int singleCoreScore;
    private int multiCoreScore;
    private int passmarkScore;
    private double cinebenchR23;
    private String testDate;

    public CpuBenchmark() {
    }

    public CpuBenchmark(int singleCoreScore, int multiCoreScore, int passmarkScore, double cinebenchR23, String testDate) {
        this.singleCoreScore = singleCoreScore;
        this.multiCoreScore = multiCoreScore;
        this.passmarkScore = passmarkScore;
        this.cinebenchR23 = cinebenchR23;
        this.testDate = testDate;
    }

    public UUID getId() {
        return id;
    }

    public int getSingleCoreScore() {
        return singleCoreScore;
    }

    public void setSingleCoreScore(int singleCoreScore) {
        this.singleCoreScore = singleCoreScore;
    }

    public int getMultiCoreScore() {
        return multiCoreScore;
    }

    public void setMultiCoreScore(int multiCoreScore) {
        this.multiCoreScore = multiCoreScore;
    }

    public int getPassmarkScore() {
        return passmarkScore;
    }

    public void setPassmarkScore(int passmarkScore) {
        this.passmarkScore = passmarkScore;
    }

    public double getCinebenchR23() {
        return cinebenchR23;
    }

    public void setCinebenchR23(double cinebenchR23) {
        this.cinebenchR23 = cinebenchR23;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    @Override
    public String toString() {
        return "CpuBenchmark{" +
                "singleCoreScore=" + singleCoreScore +
                ", multiCoreScore=" + multiCoreScore +
                ", passmarkScore=" + passmarkScore +
                ", cinebenchR23=" + cinebenchR23 +
                ", testDate='" + testDate + '\'' +
                '}';
    }
}
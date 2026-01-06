package dominik.dembski.lab05.domain;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CpuSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int cacheL1KB;
    private int cacheL2KB;
    private int cacheL3MB;
    private int tdpWatts;
    private String socketType;

    public CpuSpecification() {
    }

    public CpuSpecification(int cacheL1KB, int cacheL2KB, int cacheL3MB, int tdpWatts, String socketType) {
        this.cacheL1KB = cacheL1KB;
        this.cacheL2KB = cacheL2KB;
        this.cacheL3MB = cacheL3MB;
        this.tdpWatts = tdpWatts;
        this.socketType = socketType;
    }

    public UUID getId() {
        return id;
    }

    public int getCacheL1KB() {
        return cacheL1KB;
    }

    public void setCacheL1KB(int cacheL1KB) {
        this.cacheL1KB = cacheL1KB;
    }

    public int getCacheL2KB() {
        return cacheL2KB;
    }

    public void setCacheL2KB(int cacheL2KB) {
        this.cacheL2KB = cacheL2KB;
    }

    public int getCacheL3MB() {
        return cacheL3MB;
    }

    public void setCacheL3MB(int cacheL3MB) {
        this.cacheL3MB = cacheL3MB;
    }

    public int getTdpWatts() {
        return tdpWatts;
    }

    public void setTdpWatts(int tdpWatts) {
        this.tdpWatts = tdpWatts;
    }

    public String getSocketType() {
        return socketType;
    }

    public void setSocketType(String socketType) {
        this.socketType = socketType;
    }

    @Override
    public String toString() {
        return "CpuSpecification{" +
                "cacheL1KB=" + cacheL1KB +
                ", cacheL2KB=" + cacheL2KB +
                ", cacheL3MB=" + cacheL3MB +
                ", tdpWatts=" + tdpWatts +
                ", socketType='" + socketType + '\'' +
                '}';
    }
}
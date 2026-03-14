package com.cpu.management.domain;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CpuSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "cache_l1kb")
    private int cacheL1KB;

    @Column(name = "cache_l2kb")
    private int cacheL2KB;

    @Column(name = "cache_l3mb")
    private int cacheL3MB;

    @Column(name = "tdp_watts")
    private int tdpWatts;

    @Column(name = "socket_type")
    private String socketType;

    public CpuSpecification(int cacheL1KB, int cacheL2KB, int cacheL3MB, int tdpWatts, String socketType) {
        this.cacheL1KB = cacheL1KB;
        this.cacheL2KB = cacheL2KB;
        this.cacheL3MB = cacheL3MB;
        this.tdpWatts = tdpWatts;
        this.socketType = socketType;
    }
}
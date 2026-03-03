package com.cpu.management.exception;

import java.util.UUID;

/**
 * Wyjątek rzucany gdy CPU o podanym ID nie zostało znalezione w bazie danych.
 */
public class CpuNotFoundException extends RuntimeException {
    
    private final UUID cpuId;
    
    public CpuNotFoundException(UUID cpuId) {
        super("CPU o ID " + cpuId + " nie zostało znalezione");
        this.cpuId = cpuId;
    }
    
    public CpuNotFoundException(String model) {
        super("CPU o modelu '" + model + "' nie zostało znalezione");
        this.cpuId = null;
    }
    
    public UUID getCpuId() {
        return cpuId;
    }
}

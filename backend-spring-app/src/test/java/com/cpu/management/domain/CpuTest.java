package com.cpu.management.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CpuTest {

    @Test
    void shouldCreateCpuWithConstructor() {
        Cpu cpu = new Cpu("Ryzen 9 7950X", 16, 32, 4.5);

        assertEquals("Ryzen 9 7950X", cpu.getModel());
        assertEquals(16, cpu.getCores());
        assertEquals(32, cpu.getThreads());
        assertEquals(4.5, cpu.getFrequencyGhz());
        assertNull(cpu.getId());
        assertNull(cpu.getManufacturer());
    }

    @Test
    void shouldCreateCpuWithNoArgsConstructor() {
        Cpu cpu = new Cpu();
        assertNull(cpu.getModel());
        assertEquals(0, cpu.getCores());
    }

    @Test
    void shouldSetAndGetFields() {
        Cpu cpu = new Cpu();
        cpu.setModel("Core i9-13900K");
        cpu.setCores(24);
        cpu.setThreads(32);
        cpu.setFrequencyGhz(3.0);

        assertEquals("Core i9-13900K", cpu.getModel());
        assertEquals(24, cpu.getCores());
        assertEquals(32, cpu.getThreads());
        assertEquals(3.0, cpu.getFrequencyGhz());
    }

    @Test
    void shouldSetManufacturer() {
        Cpu cpu = new Cpu("Ryzen 5 5600X", 6, 12, 3.7);
        Manufacturer manufacturer = new Manufacturer("AMD", "USA", 1969);
        cpu.setManufacturer(manufacturer);

        assertNotNull(cpu.getManufacturer());
        assertEquals("AMD", cpu.getManufacturer().getName());
    }

    @Test
    void shouldSetTechnologies() {
        Cpu cpu = new Cpu("Ryzen 9 7950X", 16, 32, 4.5);
        Technology smt = new Technology("SMT", "Simultaneous Multithreading", 2017);
        Technology pbo = new Technology("PBO", "Precision Boost Overdrive", 2019);

        List<Technology> technologies = new ArrayList<>();
        technologies.add(smt);
        technologies.add(pbo);
        cpu.setTechnologies(technologies);

        assertEquals(2, cpu.getTechnologies().size());
    }

    @Test
    void shouldSetBenchmarks() {
        Cpu cpu = new Cpu("Core i9-13900K", 24, 32, 3.0);
        CpuBenchmark benchmark = new CpuBenchmark(2100, 30000, 45000, 38000.0, "2024-01-15");
        benchmark.setCpu(cpu);

        List<CpuBenchmark> benchmarks = new ArrayList<>();
        benchmarks.add(benchmark);
        cpu.setBenchmarks(benchmarks);

        assertEquals(1, cpu.getBenchmarks().size());
        assertEquals(2100, cpu.getBenchmarks().get(0).getSingleCoreScore());
    }

    @Test
    void shouldSetSpecification() {
        Cpu cpu = new Cpu("Ryzen 9 7950X", 16, 32, 4.5);
        CpuSpecification spec = new CpuSpecification(64, 1024, 64, 170, "AM5");
        cpu.setSpecification(spec);

        assertNotNull(cpu.getSpecification());
        assertEquals("AM5", cpu.getSpecification().getSocketType());
        assertEquals(170, cpu.getSpecification().getTdpWatts());
    }
}

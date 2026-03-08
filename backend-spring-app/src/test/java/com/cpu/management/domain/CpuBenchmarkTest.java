package com.cpu.management.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpuBenchmarkTest {

    @Test
    void shouldCreateBenchmarkWithConstructor() {
        CpuBenchmark benchmark = new CpuBenchmark(2100, 30000, 45000, 38000.0, "2024-01-15");

        assertEquals(2100, benchmark.getSingleCoreScore());
        assertEquals(30000, benchmark.getMultiCoreScore());
        assertEquals(45000, benchmark.getPassmarkScore());
        assertEquals(38000.0, benchmark.getCinebenchR23());
        assertEquals("2024-01-15", benchmark.getTestDate());
        assertNull(benchmark.getId());
    }

    @Test
    void shouldCreateBenchmarkWithNoArgsConstructor() {
        CpuBenchmark benchmark = new CpuBenchmark();
        assertEquals(0, benchmark.getSingleCoreScore());
        assertNull(benchmark.getTestDate());
    }

    @Test
    void shouldSetAndGetFields() {
        CpuBenchmark benchmark = new CpuBenchmark();
        benchmark.setSingleCoreScore(1900);
        benchmark.setMultiCoreScore(28000);
        benchmark.setPassmarkScore(42000);
        benchmark.setCinebenchR23(35000.0);
        benchmark.setTestDate("2024-06-01");

        assertEquals(1900, benchmark.getSingleCoreScore());
        assertEquals(28000, benchmark.getMultiCoreScore());
        assertEquals(42000, benchmark.getPassmarkScore());
        assertEquals(35000.0, benchmark.getCinebenchR23());
        assertEquals("2024-06-01", benchmark.getTestDate());
    }

    @Test
    void shouldSetCpuRelation() {
        CpuBenchmark benchmark = new CpuBenchmark(2100, 30000, 45000, 38000.0, "2024-01-15");
        Cpu cpu = new Cpu("Ryzen 9 7950X", 16, 32, 4.5);
        benchmark.setCpu(cpu);

        assertNotNull(benchmark.getCpu());
        assertEquals("Ryzen 9 7950X", benchmark.getCpu().getModel());
    }
}

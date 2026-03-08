package com.cpu.management.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpuSpecificationTest {

    @Test
    void shouldCreateSpecificationWithConstructor() {
        CpuSpecification spec = new CpuSpecification(64, 1024, 64, 170, "AM5");

        assertEquals(64, spec.getCacheL1KB());
        assertEquals(1024, spec.getCacheL2KB());
        assertEquals(64, spec.getCacheL3MB());
        assertEquals(170, spec.getTdpWatts());
        assertEquals("AM5", spec.getSocketType());
        assertNull(spec.getId());
    }

    @Test
    void shouldCreateSpecificationWithNoArgsConstructor() {
        CpuSpecification spec = new CpuSpecification();
        assertEquals(0, spec.getCacheL1KB());
        assertNull(spec.getSocketType());
    }

    @Test
    void shouldSetAndGetFields() {
        CpuSpecification spec = new CpuSpecification();
        spec.setCacheL1KB(80);
        spec.setCacheL2KB(2048);
        spec.setCacheL3MB(36);
        spec.setTdpWatts(125);
        spec.setSocketType("LGA1700");

        assertEquals(80, spec.getCacheL1KB());
        assertEquals(2048, spec.getCacheL2KB());
        assertEquals(36, spec.getCacheL3MB());
        assertEquals(125, spec.getTdpWatts());
        assertEquals("LGA1700", spec.getSocketType());
    }
}

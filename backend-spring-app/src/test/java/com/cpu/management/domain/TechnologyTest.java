package com.cpu.management.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TechnologyTest {

    @Test
    void shouldCreateTechnologyWithConstructor() {
        Technology technology = new Technology("SMT", "Simultaneous Multithreading", 2017);

        assertEquals("SMT", technology.getName());
        assertEquals("Simultaneous Multithreading", technology.getDescription());
        assertEquals(2017, technology.getReleaseYear());
        assertNull(technology.getId());
    }

    @Test
    void shouldCreateTechnologyWithNoArgsConstructor() {
        Technology technology = new Technology();
        assertNull(technology.getName());
        assertNull(technology.getDescription());
        assertEquals(0, technology.getReleaseYear());
    }

    @Test
    void shouldSetAndGetFields() {
        Technology technology = new Technology();
        technology.setName("DDR5");
        technology.setDescription("Latest DDR memory standard");
        technology.setReleaseYear(2020);

        assertEquals("DDR5", technology.getName());
        assertEquals("Latest DDR memory standard", technology.getDescription());
        assertEquals(2020, technology.getReleaseYear());
    }
}

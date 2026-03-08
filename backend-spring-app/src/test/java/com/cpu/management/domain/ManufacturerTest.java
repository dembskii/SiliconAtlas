package com.cpu.management.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManufacturerTest {

    @Test
    void shouldCreateManufacturerWithConstructor() {
        Manufacturer manufacturer = new Manufacturer("Intel", "USA", 1968);

        assertEquals("Intel", manufacturer.getName());
        assertEquals("USA", manufacturer.getCountry());
        assertEquals(1968, manufacturer.getFoundedYear());
        assertNull(manufacturer.getId());
    }

    @Test
    void shouldCreateManufacturerWithNoArgsConstructor() {
        Manufacturer manufacturer = new Manufacturer();
        assertNull(manufacturer.getName());
        assertNull(manufacturer.getCountry());
        assertEquals(0, manufacturer.getFoundedYear());
    }

    @Test
    void shouldSetAndGetFields() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("AMD");
        manufacturer.setCountry("USA");
        manufacturer.setFoundedYear(1969);

        assertEquals("AMD", manufacturer.getName());
        assertEquals("USA", manufacturer.getCountry());
        assertEquals(1969, manufacturer.getFoundedYear());
    }
}

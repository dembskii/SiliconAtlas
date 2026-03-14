package com.cpu.management.service;

import com.cpu.management.domain.Cpu;
import com.cpu.management.domain.Manufacturer;
import com.cpu.management.repository.CpuRepository;
import com.cpu.management.repository.ManufacturerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CpuServiceCacheEvictionTest {

    @Autowired
    private CpuService cpuService;

    @Autowired
    private CpuRepository cpuRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cpuRepository.deleteAll();
        manufacturerRepository.deleteAll();

        Cache allCpusCache = cacheManager.getCache("allCpus");
        Cache cpusCache = cacheManager.getCache("cpus");
        assertNotNull(allCpusCache);
        assertNotNull(cpusCache);
        allCpusCache.clear();
        cpusCache.clear();
    }

    @Test
    void deleteCpuShouldEvictAllCpusCacheEntry() {
        Manufacturer manufacturer = manufacturerRepository.save(new Manufacturer("AMD", "USA", 1969));
        Cpu cpu = new Cpu("Ryzen Cache Test", 8, 16, 4.0);
        cpu.setManufacturer(manufacturer);
        Cpu savedCpu = cpuRepository.save(cpu);

        // Prime cache entry under key "allCpus"
        cpuService.getAllCpus();

        Cache allCpusCache = cacheManager.getCache("allCpus");
        assertNotNull(allCpusCache);
        assertNotNull(allCpusCache.get("allCpus"));

        cpuService.deleteCpuById(savedCpu.getId());

        assertNull(allCpusCache.get("allCpus"));
        assertFalse(cpuRepository.existsById(savedCpu.getId()));
    }

    @Test
    void evictingByUuidKeyDoesNotRemoveAllCpusLiteralKey() {
        Cache allCpusCache = cacheManager.getCache("allCpus");
        assertNotNull(allCpusCache);

        allCpusCache.put("allCpus", "cached-list");
        UUID randomId = UUID.randomUUID();

        // Simulates old behavior: @CacheEvict(key = "#id") against allCpus cache
        allCpusCache.evict(randomId);

        assertNotNull(allCpusCache.get("allCpus"));
    }
}

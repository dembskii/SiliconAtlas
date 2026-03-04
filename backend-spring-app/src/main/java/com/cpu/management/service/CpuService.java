package com.cpu.management.service;

import com.cpu.management.domain.Cpu;
import com.cpu.management.domain.CpuBenchmark;
import com.cpu.management.domain.Manufacturer;
import com.cpu.management.domain.Technology;
import com.cpu.management.exception.CpuNotFoundException;
import com.cpu.management.exception.DuplicateCpuModelException;
import com.cpu.management.dto.CpuComparisonDTO;
import com.cpu.management.dto.CpuCreateDTO;
import com.cpu.management.dto.CpuDTO;
import com.cpu.management.dto.CpuPerformanceDTO;
import com.cpu.management.dto.CpuSearchCriteriaDTO;
import com.cpu.management.dto.ManufacturerStatsDTO;
import com.cpu.management.dto.PagedResponseDTO;
import com.cpu.management.mapper.EntityMapper;
import com.cpu.management.repository.CpuBenchmarkRepository;
import com.cpu.management.repository.CpuRepository;
import com.cpu.management.repository.ManufacturerRepository;
import com.cpu.management.repository.TechnologyRepository;
import com.cpu.management.specification.CpuSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CpuService {

    private final CpuRepository cpuRepository;
    private final CpuBenchmarkRepository benchmarkRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final TechnologyRepository technologyRepository;
    private final EntityMapper entityMapper;

    // =====================================================
    // PODSTAWOWE OPERACJE CRUD (zwracają DTO) - dla REST API
    // =====================================================

    @CacheEvict(value = {"allCpus", "cpus"}, allEntries = true)
    public CpuDTO addCpu(CpuCreateDTO cpuCreateDTO) {
        // Sprawdź czy model już istnieje
        if (cpuRepository.findByModel(cpuCreateDTO.getModel()).isPresent()) {
            throw new DuplicateCpuModelException(cpuCreateDTO.getModel());
        }
        Cpu cpu = entityMapper.toCpuEntity(cpuCreateDTO);
        Cpu savedCpu = cpuRepository.save(cpu);
        return entityMapper.toCpuDTO(savedCpu);
    }

    /**
     * Pobiera CPU lub rzuca wyjątek jeśli nie znaleziono.
     */
    @Cacheable(value = "cpus", key = "#id")
    public CpuDTO getCpuByIdOrThrow(UUID id) {
        return cpuRepository.findById(id)
                .map(entityMapper::toCpuDTO)
                .orElseThrow(() -> new CpuNotFoundException(id));
    }

    @Cacheable(value = "cpus", key = "#id")
    public Optional<CpuDTO> getCpuById(UUID id) {
        return cpuRepository.findById(id).map(entityMapper::toCpuDTO);
    }

    @Cacheable(value = "allCpus")
    public List<CpuDTO> getAllCpus() {
        List<Cpu> cpus = new ArrayList<>();
        cpuRepository.findAll().forEach(cpus::add);
        return entityMapper.toCpuDTOList(cpus);
    }

    @CacheEvict(value = {"allCpus", "cpus"}, key = "#id")
    public void deleteCpuById(UUID id) {
        if (!cpuRepository.existsById(id)) {
            throw new CpuNotFoundException(id);
        }
        cpuRepository.deleteById(id);
    }

    @CacheEvict(value = {"allCpus", "cpus"}, allEntries = true)
    public CpuDTO updateCpu(UUID id, CpuCreateDTO cpuCreateDTO) {
        Cpu existingCpu = cpuRepository.findById(id)
                .orElseThrow(() -> new CpuNotFoundException(id));
        
        // Sprawdź czy nowy model nie koliduje z istniejącym
        if (cpuCreateDTO.getModel() != null) {
            cpuRepository.findByModel(cpuCreateDTO.getModel())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(existing -> {
                        throw new DuplicateCpuModelException(cpuCreateDTO.getModel());
                    });
            existingCpu.setModel(cpuCreateDTO.getModel());
        }
        if (cpuCreateDTO.getCores() > 0) {
            existingCpu.setCores(cpuCreateDTO.getCores());
        }
        if (cpuCreateDTO.getThreads() > 0) {
            existingCpu.setThreads(cpuCreateDTO.getThreads());
        }
        if (cpuCreateDTO.getFrequencyGhz() > 0) {
            existingCpu.setFrequencyGhz(cpuCreateDTO.getFrequencyGhz());
        }
        Cpu savedCpu = cpuRepository.save(existingCpu);
        return entityMapper.toCpuDTO(savedCpu);
    }

    // =====================================================
    // METODY WEWNĘTRZNE (zwracają encje) - dla AdminController/Thymeleaf
    // =====================================================

    public Cpu addCpuEntity(Cpu cpu) {
        return cpuRepository.save(cpu);
    }

    public Optional<Cpu> getCpuEntityById(UUID id) {
        return cpuRepository.findById(id);
    }

    public List<Cpu> getAllCpuEntities() {
        List<Cpu> cpus = new ArrayList<>();
        cpuRepository.findAll().forEach(cpus::add);
        return cpus;
    }

    /**
     * Wyszukuje CPU według kryteriów (wersja zwracająca encje).
     */
    public List<Cpu> searchCpuEntities(CpuSearchCriteriaDTO criteria) {
        return cpuRepository.findAll(CpuSpecification.fromCriteria(criteria));
    }

    public Cpu updateCpuEntity(UUID id, Cpu cpuDetails) {
        Optional<Cpu> cpuOpt = cpuRepository.findById(id);
        if (cpuOpt.isPresent()) {
            Cpu existingCpu = cpuOpt.get();
            if (cpuDetails.getModel() != null) {
                existingCpu.setModel(cpuDetails.getModel());
            }
            if (cpuDetails.getCores() > 0) {
                existingCpu.setCores(cpuDetails.getCores());
            }
            if (cpuDetails.getThreads() > 0) {
                existingCpu.setThreads(cpuDetails.getThreads());
            }
            if (cpuDetails.getFrequencyGhz() > 0) {
                existingCpu.setFrequencyGhz(cpuDetails.getFrequencyGhz());
            }
            return cpuRepository.save(existingCpu);
        }
        return null;
    }

    public Cpu createCpuWithManufacturerAndTechnologiesEntity(Cpu cpu, UUID manufacturerId, List<UUID> technologyIds) {
        if (manufacturerId != null) {
            Optional<Manufacturer> manufacturer = manufacturerRepository.findById(manufacturerId);
            if (manufacturer.isEmpty()) {
                throw new IllegalArgumentException("Manufacturer not found with id: " + manufacturerId);
            }
            cpu.setManufacturer(manufacturer.get());
        }

        if (technologyIds != null && !technologyIds.isEmpty()) {
            List<Technology> technologies = new ArrayList<>();
            for (UUID techId : technologyIds) {
                technologyRepository.findById(techId).ifPresent(technologies::add);
            }
            cpu.setTechnologies(technologies);
        }

        return cpuRepository.save(cpu);
    }

    // =====================================================
    // ROZBUDOWANA LOGIKA BIZNESOWA (2+ repozytoriów)
    // =====================================================

    /**
     * Tworzy nowy CPU z przypisaniem do producenta i technologii.
     * Wykorzystuje: CpuRepository, ManufacturerRepository, TechnologyRepository
     */
    public CpuDTO createCpuWithManufacturerAndTechnologies(CpuCreateDTO cpuCreateDTO) {
        Cpu cpu = entityMapper.toCpuEntity(cpuCreateDTO);
        
        if (cpuCreateDTO.getManufacturerId() != null) {
            Optional<Manufacturer> manufacturer = manufacturerRepository.findById(cpuCreateDTO.getManufacturerId());
            if (manufacturer.isEmpty()) {
                throw new IllegalArgumentException("Manufacturer not found with id: " + cpuCreateDTO.getManufacturerId());
            }
            cpu.setManufacturer(manufacturer.get());
        }

        if (cpuCreateDTO.getTechnologyIds() != null && !cpuCreateDTO.getTechnologyIds().isEmpty()) {
            List<Technology> technologies = new ArrayList<>();
            for (UUID techId : cpuCreateDTO.getTechnologyIds()) {
                technologyRepository.findById(techId).ifPresent(technologies::add);
            }
            cpu.setTechnologies(technologies);
        }

        Cpu savedCpu = cpuRepository.save(cpu);
        return entityMapper.toCpuDTO(savedCpu);
    }

    /**
     * Porównuje dwa procesory pod względem specyfikacji i wydajności.
     * Wykorzystuje: CpuRepository, CpuBenchmarkRepository
     */
    public CpuComparisonDTO compareCpus(UUID cpuId1, UUID cpuId2) {
        Optional<Cpu> cpu1Opt = cpuRepository.findById(cpuId1);
        Optional<Cpu> cpu2Opt = cpuRepository.findById(cpuId2);

        if (cpu1Opt.isEmpty() || cpu2Opt.isEmpty()) {
            throw new IllegalArgumentException("One or both CPUs not found");
        }

        Cpu cpu1 = cpu1Opt.get();
        Cpu cpu2 = cpu2Opt.get();

        List<CpuBenchmark> benchmarks1 = benchmarkRepository.findByCpuId(cpuId1);
        List<CpuBenchmark> benchmarks2 = benchmarkRepository.findByCpuId(cpuId2);

        double avgScore1 = calculateAverageBenchmarkScore(benchmarks1);
        double avgScore2 = calculateAverageBenchmarkScore(benchmarks2);

        CpuComparisonDTO.CpuDetailsDTO details1 = createCpuDetails(cpu1, avgScore1);
        CpuComparisonDTO.CpuDetailsDTO details2 = createCpuDetails(cpu2, avgScore2);

        int wins1 = 0, wins2 = 0;

        if (cpu1.getCores() > cpu2.getCores()) wins1++;
        else if (cpu2.getCores() > cpu1.getCores()) wins2++;

        if (cpu1.getThreads() > cpu2.getThreads()) wins1++;
        else if (cpu2.getThreads() > cpu1.getThreads()) wins2++;

        if (cpu1.getFrequencyGhz() > cpu2.getFrequencyGhz()) wins1++;
        else if (cpu2.getFrequencyGhz() > cpu1.getFrequencyGhz()) wins2++;

        if (avgScore1 > avgScore2) wins1++;
        else if (avgScore2 > avgScore1) wins2++;

        details1.setBenchmarkWins(wins1);
        details2.setBenchmarkWins(wins2);

        String winner = wins1 > wins2 ? cpu1.getModel() : 
                       (wins2 > wins1 ? cpu2.getModel() : "Remis");

        String summary = String.format(
            "%s wygrywa w %d kategoriach, %s wygrywa w %d kategoriach. " +
            "Średni wynik benchmark: %s=%.0f, %s=%.0f",
            cpu1.getModel(), wins1, cpu2.getModel(), wins2,
            cpu1.getModel(), avgScore1, cpu2.getModel(), avgScore2
        );

        return new CpuComparisonDTO(details1, details2, winner, summary);
    }

    /**
     * Znajduje najwydajniejsze CPU dla danego producenta.
     * Wykorzystuje: CpuRepository, CpuBenchmarkRepository, ManufacturerRepository
     */
    public List<CpuPerformanceDTO> getTopPerformingCpusByManufacturer(String manufacturerName, int limit) {
        Optional<Manufacturer> manufacturer = manufacturerRepository.findByName(manufacturerName);
        if (manufacturer.isEmpty()) {
            return new ArrayList<>();
        }

        List<Cpu> cpus = cpuRepository.findCpusWithBenchmarksByManufacturer(manufacturerName);

        return cpus.stream()
            .map(this::createPerformanceDTO)
            .sorted(Comparator.comparing(CpuPerformanceDTO::getAvgPassmarkScore, 
                    Comparator.nullsLast(Comparator.reverseOrder())))
            .limit(limit)
            .collect(Collectors.toList());
    }

    /**
     * Pobiera statystyki producentów z agregacją danych.
     * Wykorzystuje: CpuRepository (JPQL GROUP BY)
     */
    @Cacheable(value = "manufacturerStats")
    public List<ManufacturerStatsDTO> getManufacturerPerformanceStats() {
        return cpuRepository.getManufacturerStatistics();
    }

    /**
     * Rekomenduje CPU na podstawie wymagań użytkownika.
     * Wykorzystuje: CpuRepository, CpuBenchmarkRepository
     */
    public List<CpuPerformanceDTO> recommendCpus(int minCores, double minFrequency, Integer minBenchmarkScore) {
        List<Cpu> candidates = cpuRepository.findByCoresGreaterThanEqualAndFrequencyGhzGreaterThanEqual(
            minCores, minFrequency);

        return candidates.stream()
            .map(cpu -> {
                List<CpuBenchmark> benchmarks = benchmarkRepository.findByCpuId(cpu.getId());
                return createPerformanceDTOWithBenchmarks(cpu, benchmarks);
            })
            .filter(dto -> {
                if (minBenchmarkScore != null && dto.getAvgPassmarkScore() != null) {
                    return dto.getAvgPassmarkScore() >= minBenchmarkScore;
                }
                return true;
            })
            .sorted(Comparator.comparing(CpuPerformanceDTO::getAvgPassmarkScore, 
                    Comparator.nullsLast(Comparator.reverseOrder())))
            .collect(Collectors.toList());
    }

    // =====================================================
    // METODY POMOCNICZE
    // =====================================================

    private double calculateAverageBenchmarkScore(List<CpuBenchmark> benchmarks) {
        if (benchmarks == null || benchmarks.isEmpty()) {
            return 0.0;
        }
        return benchmarks.stream()
            .mapToInt(CpuBenchmark::getPassmarkScore)
            .average()
            .orElse(0.0);
    }

    private CpuComparisonDTO.CpuDetailsDTO createCpuDetails(Cpu cpu, double avgScore) {
        CpuComparisonDTO.CpuDetailsDTO details = new CpuComparisonDTO.CpuDetailsDTO();
        details.setId(cpu.getId());
        details.setModel(cpu.getModel());
        details.setManufacturer(cpu.getManufacturer() != null ? cpu.getManufacturer().getName() : null);
        details.setCores(cpu.getCores());
        details.setThreads(cpu.getThreads());
        details.setFrequencyGhz(cpu.getFrequencyGhz());
        details.setAvgBenchmarkScore(avgScore);
        if (cpu.getSpecification() != null) {
            details.setTdpWatts(cpu.getSpecification().getTdpWatts());
        }
        return details;
    }

    private CpuPerformanceDTO createPerformanceDTO(Cpu cpu) {
        List<CpuBenchmark> benchmarks = cpu.getBenchmarks() != null ? cpu.getBenchmarks() : new ArrayList<>();
        return createPerformanceDTOWithBenchmarks(cpu, benchmarks);
    }

    private CpuPerformanceDTO createPerformanceDTOWithBenchmarks(Cpu cpu, List<CpuBenchmark> benchmarks) {
        CpuPerformanceDTO dto = new CpuPerformanceDTO();
        dto.setCpuId(cpu.getId());
        dto.setCpuModel(cpu.getModel());
        dto.setManufacturerName(cpu.getManufacturer() != null ? cpu.getManufacturer().getName() : null);
        dto.setCores(cpu.getCores());
        dto.setThreads(cpu.getThreads());
        dto.setFrequencyGhz(cpu.getFrequencyGhz());
        dto.setBenchmarkCount((long) benchmarks.size());

        if (!benchmarks.isEmpty()) {
            dto.setAvgSingleCoreScore(benchmarks.stream()
                .mapToInt(CpuBenchmark::getSingleCoreScore).average().orElse(0));
            dto.setAvgMultiCoreScore(benchmarks.stream()
                .mapToInt(CpuBenchmark::getMultiCoreScore).average().orElse(0));
            dto.setAvgPassmarkScore(benchmarks.stream()
                .mapToInt(CpuBenchmark::getPassmarkScore).average().orElse(0));
            dto.setMaxCinebenchR23(benchmarks.stream()
                .mapToDouble(CpuBenchmark::getCinebenchR23).max().orElse(0));
        }

        return dto;
    }

    // =====================================================
    // WIELOKRYTERIALNA WYSZUKIWARKA Z PAGINACJĄ
    // =====================================================

    /**
     * Wyszukuje CPU według wielu kryteriów z obsługą paginacji i sortowania.
     * 
     * @param criteria kryteria wyszukiwania (wszystkie opcjonalne)
     * @param page numer strony (0-indexed)
     * @param size rozmiar strony
     * @param sortBy pole do sortowania (model, cores, threads, frequencyGhz)
     * @param sortDir kierunek sortowania (asc, desc)
     * @return stronicowana odpowiedź z listą CPU jako DTO
     */
    public PagedResponseDTO<CpuDTO> searchCpus(CpuSearchCriteriaDTO criteria, 
                                             int page, int size, 
                                             String sortBy, String sortDir) {
        // Walidacja i domyślne wartości
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 10;
        if (sortBy == null || sortBy.isEmpty()) sortBy = "model";
        if (sortDir == null || sortDir.isEmpty()) sortDir = "asc";

        // Utworzenie obiektu sortowania
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();

        // Utworzenie obiektu paginacji
        Pageable pageable = PageRequest.of(page, size, sort);

        // Wykonanie zapytania z wykorzystaniem Specification
        Page<Cpu> cpuPage = cpuRepository.findAll(
            CpuSpecification.fromCriteria(criteria), 
            pageable
        );

        // Konwersja do DTO odpowiedzi
        PagedResponseDTO<CpuDTO> response = new PagedResponseDTO<>();
        response.setContent(entityMapper.toCpuDTOList(cpuPage.getContent()));
        response.setPageNumber(cpuPage.getNumber());
        response.setPageSize(cpuPage.getSize());
        response.setTotalElements(cpuPage.getTotalElements());
        response.setTotalPages(cpuPage.getTotalPages());
        response.setFirst(cpuPage.isFirst());
        response.setLast(cpuPage.isLast());
        response.setHasNext(cpuPage.hasNext());
        response.setHasPrevious(cpuPage.hasPrevious());
        return response;
    }

    /**
     * Przypisuje producenta do istniejącego CPU
     */
    public void assignManufacturerToCpu(UUID cpuId, UUID manufacturerId) {
        Optional<Cpu> cpuOpt = cpuRepository.findById(cpuId);
        Optional<Manufacturer> manufacturerOpt = manufacturerRepository.findById(manufacturerId);
        
        if (cpuOpt.isPresent() && manufacturerOpt.isPresent()) {
            Cpu cpu = cpuOpt.get();
            cpu.setManufacturer(manufacturerOpt.get());
            cpuRepository.save(cpu);
        }
    }

    /**
     * Przypisuje technologie do istniejącego CPU
     */
    public void assignTechnologiesToCpu(UUID cpuId, List<UUID> technologyIds) {
        Optional<Cpu> cpuOpt = cpuRepository.findById(cpuId);
        if (cpuOpt.isPresent()) {
            Cpu cpu = cpuOpt.get();
            List<Technology> technologies = new ArrayList<>();
            for (UUID techId : technologyIds) {
                technologyRepository.findById(techId).ifPresent(technologies::add);
            }
            cpu.setTechnologies(technologies);
            cpuRepository.save(cpu);
        }
    }

    /**
     * Pobiera wszystkie CPU z paginacją (bez filtrowania).
     */
    public PagedResponseDTO<CpuDTO> getAllCpusPaged(int page, int size, String sortBy, String sortDir) {
        return searchCpus(new CpuSearchCriteriaDTO(), page, size, sortBy, sortDir);
    }
}

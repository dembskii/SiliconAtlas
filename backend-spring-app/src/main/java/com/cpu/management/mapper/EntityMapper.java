package com.cpu.management.mapper;

import com.cpu.management.domain.Cpu;
import com.cpu.management.domain.CpuBenchmark;
import com.cpu.management.domain.CpuSpecification;
import com.cpu.management.domain.Manufacturer;
import com.cpu.management.domain.Technology;
import com.cpu.management.dto.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public CpuDTO toCpuDTO(Cpu cpu) {
        if (cpu == null) return null;
        
        return CpuDTO.builder()
                .id(cpu.getId())
                .model(cpu.getModel())
                .cores(cpu.getCores())
                .threads(cpu.getThreads())
                .frequencyGhz(cpu.getFrequencyGhz())
                .manufacturerName(cpu.getManufacturer() != null ? cpu.getManufacturer().getName() : null)
                .manufacturerId(cpu.getManufacturer() != null ? cpu.getManufacturer().getId() : null)
                .technologyNames(cpu.getTechnologies() != null 
                        ? cpu.getTechnologies().stream().map(Technology::getName).collect(Collectors.toList())
                        : Collections.emptyList())
                .technologyIds(cpu.getTechnologies() != null
                        ? cpu.getTechnologies().stream().map(Technology::getId).collect(Collectors.toList())
                        : Collections.emptyList())
                .specification(cpu.getSpecification() != null ? toCpuSpecificationDTO(cpu.getSpecification()) : null)
                .build();
    }

    public List<CpuDTO> toCpuDTOList(List<Cpu> cpus) {
        if (cpus == null) return Collections.emptyList();
        return cpus.stream().map(this::toCpuDTO).collect(Collectors.toList());
    }

    public Cpu toCpuEntity(CpuCreateDTO dto) {
        if (dto == null) return null;
        
        Cpu cpu = new Cpu();
        cpu.setModel(dto.getModel());
        cpu.setCores(dto.getCores());
        cpu.setThreads(dto.getThreads());
        cpu.setFrequencyGhz(dto.getFrequencyGhz());
        return cpu;
    }

    public ManufacturerDTO toManufacturerDTO(Manufacturer manufacturer) {
        if (manufacturer == null) return null;
        
        return ManufacturerDTO.builder()
                .id(manufacturer.getId())
                .name(manufacturer.getName())
                .country(manufacturer.getCountry())
                .foundedYear(manufacturer.getFoundedYear())
                .cpuCount(manufacturer.getCpus() != null ? manufacturer.getCpus().size() : 0)
                .build();
    }

    public List<ManufacturerDTO> toManufacturerDTOList(List<Manufacturer> manufacturers) {
        if (manufacturers == null) return Collections.emptyList();
        return manufacturers.stream().map(this::toManufacturerDTO).collect(Collectors.toList());
    }

    public Manufacturer toManufacturerEntity(ManufacturerCreateDTO dto) {
        if (dto == null) return null;
        
        return new Manufacturer(dto.getName(), dto.getCountry(), dto.getFoundedYear());
    }

    public TechnologyDTO toTechnologyDTO(Technology technology) {
        if (technology == null) return null;
        
        return TechnologyDTO.builder()
                .id(technology.getId())
                .name(technology.getName())
                .description(technology.getDescription())
                .releaseYear(technology.getReleaseYear())
                .cpuCount(technology.getCpus() != null ? technology.getCpus().size() : 0)
                .build();
    }

    public List<TechnologyDTO> toTechnologyDTOList(List<Technology> technologies) {
        if (technologies == null) return Collections.emptyList();
        return technologies.stream().map(this::toTechnologyDTO).collect(Collectors.toList());
    }

    public Technology toTechnologyEntity(TechnologyCreateDTO dto) {
        if (dto == null) return null;
        
        return new Technology(dto.getName(), dto.getDescription(), dto.getReleaseYear());
    }

    public CpuBenchmarkDTO toCpuBenchmarkDTO(CpuBenchmark benchmark) {
        if (benchmark == null) return null;
        
        return CpuBenchmarkDTO.builder()
                .id(benchmark.getId())
                .singleCoreScore(benchmark.getSingleCoreScore())
                .multiCoreScore(benchmark.getMultiCoreScore())
                .passmarkScore(benchmark.getPassmarkScore())
                .cinebenchR23(benchmark.getCinebenchR23())
                .testDate(benchmark.getTestDate())
                .cpuId(benchmark.getCpu() != null ? benchmark.getCpu().getId() : null)
                .cpuModel(benchmark.getCpu() != null ? benchmark.getCpu().getModel() : null)
                .build();
    }

    public List<CpuBenchmarkDTO> toCpuBenchmarkDTOList(List<CpuBenchmark> benchmarks) {
        if (benchmarks == null) return Collections.emptyList();
        return benchmarks.stream().map(this::toCpuBenchmarkDTO).collect(Collectors.toList());
    }

    public CpuBenchmark toCpuBenchmarkEntity(CpuBenchmarkCreateDTO dto) {
        if (dto == null) return null;
        
        return new CpuBenchmark(
                dto.getSingleCoreScore(),
                dto.getMultiCoreScore(),
                dto.getPassmarkScore(),
                dto.getCinebenchR23(),
                dto.getTestDate()
        );
    }

    public CpuSpecificationDTO toCpuSpecificationDTO(CpuSpecification specification) {
        if (specification == null) return null;
        
        return CpuSpecificationDTO.builder()
                .id(specification.getId())
                .cacheL1KB(specification.getCacheL1KB())
                .cacheL2KB(specification.getCacheL2KB())
                .cacheL3MB(specification.getCacheL3MB())
                .tdpWatts(specification.getTdpWatts())
                .socketType(specification.getSocketType())
                .build();
    }

    public List<CpuSpecificationDTO> toCpuSpecificationDTOList(List<CpuSpecification> specifications) {
        if (specifications == null) return Collections.emptyList();
        return specifications.stream().map(this::toCpuSpecificationDTO).collect(Collectors.toList());
    }

    public CpuSpecification toCpuSpecificationEntity(CpuSpecificationCreateDTO dto) {
        if (dto == null) return null;
        
        return new CpuSpecification(
                dto.getCacheL1KB(),
                dto.getCacheL2KB(),
                dto.getCacheL3MB(),
                dto.getTdpWatts(),
                dto.getSocketType()
        );
    }

    public <T, D> PagedResponseDTO<D> toPagedResponseDTO(PagedResponseDTO<T> source, List<D> content) {
        PagedResponseDTO<D> result = new PagedResponseDTO<>();
        result.setContent(content);
        result.setPageNumber(source.getPageNumber());
        result.setPageSize(source.getPageSize());
        result.setTotalElements(source.getTotalElements());
        result.setTotalPages(source.getTotalPages());
        result.setFirst(source.isFirst());
        result.setLast(source.isLast());
        result.setHasNext(source.isHasNext());
        result.setHasPrevious(source.isHasPrevious());
        return result;
    }
}

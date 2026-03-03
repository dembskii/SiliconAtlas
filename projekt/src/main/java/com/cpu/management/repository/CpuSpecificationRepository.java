package com.cpu.management.repository;

import com.cpu.management.domain.CpuSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CpuSpecificationRepository extends JpaRepository<CpuSpecification, UUID> {
    
    List<CpuSpecification> findBySocketType(String socketType);
    
    List<CpuSpecification> findByTdpWattsLessThan(int tdp);
}

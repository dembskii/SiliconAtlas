package dominik.dembski.lab05.cpu.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dominik.dembski.lab05.cpu.domain.Cpu;

@Repository
public interface CpuRepository extends JpaRepository<Cpu, UUID> {
}

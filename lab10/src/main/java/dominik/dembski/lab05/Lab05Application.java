package dominik.dembski.lab05;

import dominik.dembski.lab05.domain.Cpu;
import dominik.dembski.lab05.domain.CpuBenchmark;
import dominik.dembski.lab05.domain.CpuSpecification;
import dominik.dembski.lab05.domain.Manufacturer;
import dominik.dembski.lab05.service.CpuService;
import dominik.dembski.lab05.service.CpuBenchmarkService;
import dominik.dembski.lab05.service.CpuSpecificationService;
import dominik.dembski.lab05.service.ManufacturerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Lab05Application {

	public static void main(String[] args) {
		SpringApplication.run(Lab05Application.class, args);
	}

	@Bean
	public CommandLineRunner setUpApp(CpuService cpuService,
									   CpuBenchmarkService benchmarkService,
									   CpuSpecificationService specificationService,
									   ManufacturerService manufacturerService) {
		return args -> {
			// Dodanie producenta przy użyciu @Builder
			Manufacturer manufacturer = Manufacturer.builder()
				.name("Intel")
				.country("USA")
				.foundedYear(1968)
				.build();
			Manufacturer savedManufacturer = manufacturerService.addManufacturer(manufacturer);
			System.out.println("Dodano producenta: " + savedManufacturer);

			// Dodanie specyfikacji
			CpuSpecification specification = CpuSpecification.builder()
				.cacheL1KB(256)
				.cacheL2KB(1024)
				.cacheL3MB(12)
				.tdpWatts(65)
				.socketType("LGA1700")
				.build();
			CpuSpecification savedSpec = specificationService.addSpecification(specification);
			System.out.println("Dodano specyfikację: " + savedSpec);

			// Dodanie benchmarku
			CpuBenchmark benchmark = CpuBenchmark.builder()
				.singleCoreScore(1800)
				.multiCoreScore(8500)
				.passmarkScore(45000)
				.cinebenchR23(1850.5)
				.testDate("2024-01-15")
				.build();
			CpuBenchmark savedBenchmark = benchmarkService.addBenchmark(benchmark);
			System.out.println("Dodano benchmark: " + savedBenchmark);

			// Dodanie procesora
			Cpu cpu = Cpu.builder()
				.model("Intel Core i7-13700K")
				.cores(16)
				.threads(24)
				.frequencyGhz(3.4)
				.manufacturer(savedManufacturer)
				.specification(savedSpec)
				.build();
			Cpu savedCpu = cpuService.addCpu(cpu);
			System.out.println("Dodano CPU: " + savedCpu);

			// Pobranie procesora po ID
			var retrievedCpu = cpuService.getCpuById(savedCpu.getId());
			if (retrievedCpu.isPresent()) {
				System.out.println("Pobrano CPU: " + retrievedCpu.get());
			}

			// Pobranie wszystkich procesorów
			System.out.println("Wszystkie CPU w systemie:");
			cpuService.getAllCpus().forEach(c -> System.out.println("  - " + c));

			// Pobranie wszystkich producentów
			System.out.println("Wszyscy producenci w systemie:");
			manufacturerService.getAllManufacturers().forEach(m -> System.out.println("  - " + m));

			// Pobranie wszystkich specyfikacji
			System.out.println("Wszystkie specyfikacje w systemie:");
			specificationService.getAllSpecifications().forEach(s -> System.out.println("  - " + s));

			// Pobranie wszystkich benchmarków
			System.out.println("Wszystkie benchmarki w systemie:");
			benchmarkService.getAllBenchmarks().forEach(b -> System.out.println("  - " + b));

			// Aktualizacja procesora
			Cpu updateData = Cpu.builder()
				.model("Intel Core i9-13900K")
				.frequencyGhz(3.6)
				.build();
			Cpu updatedCpu = cpuService.updateCpu(savedCpu.getId(), updateData);
			System.out.println("Zaktualizowano CPU: " + updatedCpu);

			// Usunięcie benchmarku
			benchmarkService.deleteBenchmarkById(savedBenchmark.getId());
			System.out.println("Usunięto benchmark o ID: " + savedBenchmark.getId());
		};
	}

}

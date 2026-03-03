package com.cpu.management;

import com.cpu.management.domain.Cpu;
import com.cpu.management.domain.CpuBenchmark;
import com.cpu.management.domain.CpuSpecification;
import com.cpu.management.domain.Manufacturer;
import com.cpu.management.domain.Technology;
import com.cpu.management.service.CpuService;
import com.cpu.management.service.CpuBenchmarkService;
import com.cpu.management.service.CpuSpecificationService;
import com.cpu.management.service.ManufacturerService;
import com.cpu.management.service.TechnologyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;


@SpringBootApplication
public class CpuManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CpuManagementApplication.class, args);
	}

	@Bean
	public CommandLineRunner setUpApp(CpuService cpuService,
									   CpuBenchmarkService benchmarkService,
									   CpuSpecificationService specificationService,
									   ManufacturerService manufacturerService,
									   TechnologyService technologyService) {
		return args -> {
			// ===== PRODUCENCI =====
			System.out.println("========== INICJALIZACJA PRODUCENTÓW ==========");
			
			Manufacturer intel = new Manufacturer("Intel", "USA", 1968);
			Manufacturer savedIntel = manufacturerService.addManufacturerEntity(intel);
			System.out.println("Dodano producenta: " + savedIntel);
			
			Manufacturer amd = new Manufacturer("AMD", "USA", 1969);
			Manufacturer savedAmd = manufacturerService.addManufacturerEntity(amd);
			System.out.println("Dodano producenta: " + savedAmd);
			
			// ===== TECHNOLOGIE =====
			System.out.println("\n========== INICJALIZACJA TECHNOLOGII ==========");
			
			Technology hyper = new Technology("Hyper-Threading", "Technologia wielowątkowości Intela", 2002);
			Technology savedHyper = technologyService.addTechnologyEntity(hyper);
			System.out.println("Dodano technologię: " + savedHyper);
			
			Technology smt = new Technology("SMT", "Simultaneous Multithreading - wielowątkowość AMD", 2003);
			Technology savedSmt = technologyService.addTechnologyEntity(smt);
			System.out.println("Dodano technologię: " + savedSmt);
			
			Technology rdna = new Technology("RDNA", "Architektura RDNA dla procesorów AMD", 2019);
			Technology savedRdna = technologyService.addTechnologyEntity(rdna);
			System.out.println("Dodano technologię: " + savedRdna);
			
			// Pobierz technologie z bazy - to są attached entities
			Technology hyperRefresh = technologyService.getTechnologyEntityById(savedHyper.getId()).get();
			Technology smtRefresh = technologyService.getTechnologyEntityById(savedSmt.getId()).get();
			Technology rdnaRefresh = technologyService.getTechnologyEntityById(savedRdna.getId()).get();
			
			// ===== PROCESORY INTEL =====
			System.out.println("\n========== INICJALIZACJA PROCESORÓW INTEL ==========");
			
			// Intel CPU 1: Core i9-13900K
			Cpu intel1 = new Cpu("Intel Core i9-13900K", 24, 32, 3.0);
			CpuSpecification spec1 = new CpuSpecification(36, 1440, 24, 125, "LGA1700");
			intel1.setSpecification(spec1);
			Cpu savedIntel1 = cpuService.addCpuEntity(intel1);
			cpuService.assignManufacturerToCpu(savedIntel1.getId(), savedIntel.getId());
			cpuService.assignTechnologiesToCpu(savedIntel1.getId(), Arrays.asList(hyperRefresh.getId()));
			System.out.println("Dodano CPU: " + savedIntel1);
			
			// Pobierz CPU z bazy dla benchmarków
			Cpu intel1FromDb = cpuService.getCpuEntityById(savedIntel1.getId()).get();
			CpuBenchmark benchmark1_1 = new CpuBenchmark(2150, 16800, 58000, 2180.5, "2024-01-15");
			benchmark1_1.setCpu(intel1FromDb);
			benchmarkService.addBenchmarkEntity(benchmark1_1);
			System.out.println("Dodano benchmark 1 do Intel i9-13900K");
			
			Cpu intel1FromDb2 = cpuService.getCpuEntityById(savedIntel1.getId()).get();
			CpuBenchmark benchmark1_2 = new CpuBenchmark(2200, 17200, 59500, 2250.0, "2024-02-01");
			benchmark1_2.setCpu(intel1FromDb2);
			benchmarkService.addBenchmarkEntity(benchmark1_2);
			System.out.println("Dodano benchmark 2 do Intel i9-13900K");
			
			// Intel CPU 2: Core i7-13700K
			Cpu intel2 = new Cpu("Intel Core i7-13700K", 16, 24, 3.4);
			CpuSpecification spec2 = new CpuSpecification(30, 960, 20, 125, "LGA1700");
			intel2.setSpecification(spec2);
			Cpu savedIntel2 = cpuService.addCpuEntity(intel2);
			cpuService.assignManufacturerToCpu(savedIntel2.getId(), savedIntel.getId());
			cpuService.assignTechnologiesToCpu(savedIntel2.getId(), Arrays.asList(hyperRefresh.getId()));
			System.out.println("Dodano CPU: " + savedIntel2);
			
			Cpu intel2FromDb = cpuService.getCpuEntityById(savedIntel2.getId()).get();
			CpuBenchmark benchmark2_1 = new CpuBenchmark(1950, 13500, 48000, 1980.0, "2024-01-10");
			benchmark2_1.setCpu(intel2FromDb);
			benchmarkService.addBenchmarkEntity(benchmark2_1);
			System.out.println("Dodano benchmark do Intel i7-13700K");
			
			// Intel CPU 3: Core i5-13600K
			Cpu intel3 = new Cpu("Intel Core i5-13600K", 14, 20, 3.5);
			CpuSpecification spec3 = new CpuSpecification(24, 768, 16, 125, "LGA1700");
			intel3.setSpecification(spec3);
			Cpu savedIntel3 = cpuService.addCpuEntity(intel3);
			cpuService.assignManufacturerToCpu(savedIntel3.getId(), savedIntel.getId());
			cpuService.assignTechnologiesToCpu(savedIntel3.getId(), Arrays.asList(hyperRefresh.getId()));
			System.out.println("Dodano CPU: " + savedIntel3);
			
			Cpu intel3FromDb = cpuService.getCpuEntityById(savedIntel3.getId()).get();
			CpuBenchmark benchmark3_1 = new CpuBenchmark(1750, 10500, 40000, 1780.0, "2024-01-12");
			benchmark3_1.setCpu(intel3FromDb);
			benchmarkService.addBenchmarkEntity(benchmark3_1);
			System.out.println("Dodano benchmark do Intel i5-13600K");
			
			// ===== PROCESORY AMD =====
			System.out.println("\n========== INICJALIZACJA PROCESORÓW AMD ==========");
			
			// AMD CPU 1: Ryzen 9 7950X
			Cpu amd1 = new Cpu("AMD Ryzen 9 7950X", 16, 32, 4.5);
			CpuSpecification spec4 = new CpuSpecification(64, 2048, 16, 170, "AM5");
			amd1.setSpecification(spec4);
			Cpu savedAmd1 = cpuService.addCpuEntity(amd1);
			cpuService.assignManufacturerToCpu(savedAmd1.getId(), savedAmd.getId());
			cpuService.assignTechnologiesToCpu(savedAmd1.getId(), Arrays.asList(smtRefresh.getId(), rdnaRefresh.getId()));
			System.out.println("Dodano CPU: " + savedAmd1);
			
			Cpu amd1FromDb = cpuService.getCpuEntityById(savedAmd1.getId()).get();
			CpuBenchmark benchmark4_1 = new CpuBenchmark(2050, 16200, 56000, 2100.0, "2024-01-18");
			benchmark4_1.setCpu(amd1FromDb);
			benchmarkService.addBenchmarkEntity(benchmark4_1);
			System.out.println("Dodano benchmark 1 do AMD Ryzen 9 7950X");
			
			Cpu amd1FromDb2 = cpuService.getCpuEntityById(savedAmd1.getId()).get();
			CpuBenchmark benchmark4_2 = new CpuBenchmark(2100, 16500, 57000, 2150.0, "2024-02-05");
			benchmark4_2.setCpu(amd1FromDb2);
			benchmarkService.addBenchmarkEntity(benchmark4_2);
			System.out.println("Dodano benchmark 2 do AMD Ryzen 9 7950X");
			
			// AMD CPU 2: Ryzen 7 7700X
			Cpu amd2 = new Cpu("AMD Ryzen 7 7700X", 8, 16, 4.5);
			CpuSpecification spec5 = new CpuSpecification(32, 1024, 12, 105, "AM5");
			amd2.setSpecification(spec5);
			Cpu savedAmd2 = cpuService.addCpuEntity(amd2);
			cpuService.assignManufacturerToCpu(savedAmd2.getId(), savedAmd.getId());
			cpuService.assignTechnologiesToCpu(savedAmd2.getId(), Arrays.asList(smtRefresh.getId(), rdnaRefresh.getId()));
			System.out.println("Dodano CPU: " + savedAmd2);
			
			Cpu amd2FromDb = cpuService.getCpuEntityById(savedAmd2.getId()).get();
			CpuBenchmark benchmark5_1 = new CpuBenchmark(1850, 12800, 44000, 1900.0, "2024-01-20");
			benchmark5_1.setCpu(amd2FromDb);
			benchmarkService.addBenchmarkEntity(benchmark5_1);
			System.out.println("Dodano benchmark do AMD Ryzen 7 7700X");
			
			// ===== WYŚWIETLENIE DANYCH =====
			System.out.println("\n========== PODSUMOWANIE ==========");
			
			System.out.println("\nWszyscy producenci:");
			manufacturerService.getAllManufacturerEntities().forEach(m -> System.out.println("  - " + m));
			
			System.out.println("\nWszystkie technologie:");
			technologyService.getAllTechnologyEntities().forEach(t -> System.out.println("  - " + t));
			
			System.out.println("\nWszystkie procesory:");
			cpuService.getAllCpuEntities().forEach(c -> System.out.println("  - " + c));
			
			System.out.println("\nWszystkie benchmarki:");
			benchmarkService.getAllBenchmarkEntities().forEach(b -> System.out.println("  - " + b));
			
			System.out.println("\n========== INICJALIZACJA ZAKOŃCZONA ==========\n");
		};
	}

}
